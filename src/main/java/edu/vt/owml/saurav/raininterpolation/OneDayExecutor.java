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
package edu.vt.owml.saurav.raininterpolation;

import edu.vt.owml.saurav.raininterpolation.database.DatabaseWorker;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A threaded method to drive one day of interpolation.
 *
 * @author saurav
 */
public class OneDayExecutor implements Callable {

    private Connection conn;
    long date;
    DistanceStore ds;
    String rainTable;
    String resultTable;
    IDWInterpolator idw;
    List<Integer> grids;

    /**
     *
     * @param date
     * @param ds
     * @param rainTable
     * @param resultTable
     * @param idw
     * @param grids
     * @throws SQLException
     */
    public OneDayExecutor(long date, DistanceStore ds, String rainTable, String resultTable, IDWInterpolator idw, List<Integer> grids) throws SQLException {
        this.date = date;
        this.ds = ds;
        this.rainTable = rainTable;
        this.resultTable = resultTable;
        this.idw = idw;
        this.grids = grids;
    }

    /**
     * run() is not the override method for Callable interface, call is.
     */
    private void run() {
        try {
            conn = DatabaseWorker.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("Select * from " + rainTable + " where date1=" + date);
            List<String> labels = new ArrayList();
            List<Double> rainVal = new ArrayList();
            //System.out.println("date = " + date);
            while (rs.next()) {
                String stationLabels = rs.getString(1);
                double val = rs.getDouble(3);
                labels.add(stationLabels);
                rainVal.add(val);
            }
            for (Integer i : grids) {
                List<Double> distances = new ArrayList();
                for (String label : labels) {
                    distances.add(ds.getDistance(label, i));
                }
                double findValueAt = idw.findValueAt(rainVal, distances);
                //System.out.println("findValueAt = " + findValueAt);
                conn.createStatement().execute("Insert into " + resultTable + " values (" + date + "," + i + "," + findValueAt + ")");
                //testing code
                if (i % 180 == 0) {
                    LOG.log(Level.FINE, "{0}\t{1}\t{2}\t{3}\t{4}", new Object[]{i, date, findValueAt, expandArray(rainVal), expandArray(distances)});
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(OneDayExecutor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                DatabaseWorker.returnConnection(conn);
            } catch (SQLException ex) {
                Logger.getLogger(OneDayExecutor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String expandArray(List<Double> dd) {
        StringBuffer s = new StringBuffer("");
        for (Double d : dd) {
            s = s.append(d.toString()).append(",");
        }
        return s.toString();
    }

    @Override
    public Object call() throws Exception {
        run();
        return this;
    }
    private static final Logger LOG = Logger.getLogger(OneDayExecutor.class.getName());

}
