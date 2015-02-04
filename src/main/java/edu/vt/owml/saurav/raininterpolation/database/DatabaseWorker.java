/*
 * Copyright (C) 2015 saurav
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.vt.owml.saurav.raininterpolation.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.h2.tools.Server;

/**
 * A database worker to get connections to the in memory database. Use by the
 * threads.
 *
 * @author saurav
 */
public class DatabaseWorker {

    private Server server;
    private static Queue<Connection> connections = new LinkedList<>();
    public String URL;

    /**
     * Start a database engine along with a TCP server
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public DatabaseWorker() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection conn = DatabaseWorker.getConnection();
        // start a TCP server
        server = Server.createTcpServer().start();
        System.out.println("Server started and connection is open.");
        System.out.println("URL: jdbc:h2:" + server.getURL() + "/mem:rain");
        URL = "jdbc:h2:" + server.getURL() + "/mem:rain";
        connections.offer(conn);
    }

    public static Connection getConnection() throws SQLException {
        return (connections.isEmpty()) ? DriverManager.getConnection("jdbc:h2:mem:rain") : connections.poll();
    }

    public static void returnConnection(Connection conn) throws SQLException {
        connections.offer(conn);
    }

    public void killServer() throws SQLException {
        server.stop();

    }

    /**
     *
     * @param csvFile
     * @param tableName
     * @throws SQLException
     */
    public void loadrainCSVinTable(String csvFile, String tableName) throws SQLException {
        Connection conn = DatabaseWorker.getConnection();
        conn.createStatement().execute("DROP TABLE IF EXISTS " + tableName);
        conn.createStatement().execute("CREATE TABLE " + tableName + " (station varchar(255), date1 int, val1 double, "
                + "Primary key(station,date1)) AS SELECT * FROM CSVREAD('" + csvFile + "');");
        //Zero is a signal cannot delete zeros
        //conn.createStatement().execute("DELETE FROM " + tableName + " where val1=0");
        DatabaseWorker.returnConnection(conn);
        System.out.println("Data Loaded");
    }

    /**
     *
     * @param csvFile
     * @param tableName
     * @param resultstableName
     * @throws SQLException
     */
    public void saveResults(String csvFile, String tableName, String resultstableName) throws SQLException {
        Connection conn = DatabaseWorker.getConnection();
        conn.createStatement().execute("DROP TABLE IF EXISTS " + tableName);
        conn.createStatement().execute("create table " + tableName + " ( date1 int, val1 double, Primary key(date1)) as select DATE1, AVG(VAL1) from " + resultstableName + " group by Date1  ");
        conn.createStatement().execute("CALL CSVWRITE('" + csvFile + ".csv" + "', 'SELECT * FROM " + tableName + "')");
        DatabaseWorker.returnConnection(conn);
        System.out.println("Data Loaded");
    }

    /**
     *
     * @param tableName
     * @return
     * @throws SQLException
     */
    public List<RainSummaryData> summarizeRainTable(String tableName) throws SQLException {
        List<RainSummaryData> ls = new ArrayList();
        Connection conn = DatabaseWorker.getConnection();
        ResultSet rs = conn.createStatement().executeQuery("Select Station, count(val1) as count, min(date1) as start, Max(date1) as end from " + tableName + " group by Station order by Station ");
        while (rs.next()) {
            RainSummaryData rsd = new RainSummaryData(rs.getString(1), rs.getLong(2), rs.getLong(3), rs.getLong(4));
            ls.add(rsd);
            System.out.println(rsd);
        }
        DatabaseWorker.returnConnection(conn);
        return ls;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public class RainSummaryData {

        String station;
        long count;
        long mindate;
        long maxdate;

        public RainSummaryData(String station, long count, long mindate, long maxdate) {
            this.station = station;
            this.count = count;
            this.mindate = mindate;
            this.maxdate = maxdate;
        }

        @Override
        public String toString() {

            return String.format("%-35s %8d %8d %8d", station.length() > 34 ? station.substring(0, 30) + " ..." : station, count, mindate, maxdate);
        }

        public String getStation() {
            return station;
        }

        public void setStation(String station) {
            this.station = station;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public long getMindate() {
            return mindate;
        }

        public void setMindate(long mindate) {
            this.mindate = mindate;
        }

        public long getMaxdate() {
            return maxdate;
        }

        public void setMaxdate(long maxdate) {
            this.maxdate = maxdate;
        }

    }
}
