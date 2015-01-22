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

import edu.vt.owml.saurav.raininterpolation.GUI.GUIInputStore;
import edu.vt.owml.saurav.raininterpolation.GUI.ProgressUpdateEvent;
import edu.vt.owml.saurav.raininterpolation.database.DatabaseWorker;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingWorker;
import javax.swing.event.EventListenerList;

/**
 *
 * @author saurav
 */
public class OverAllExecutor extends SwingWorker<Void, OneDayExecutor> {

    protected EventListenerList listenerList = new EventListenerList();

    public void addProgressUpdateEventListener(ProgressUpdateEvent.ProgressUpdateEventListener listener) {
        listenerList.add(ProgressUpdateEvent.ProgressUpdateEventListener.class, listener);
    }

    public void removeProgressUpdateEventListener(ProgressUpdateEvent.ProgressUpdateEventListener listener) {
        listenerList.remove(ProgressUpdateEvent.ProgressUpdateEventListener.class, listener);
    }

    private void fireProgressUpdateEvent(ProgressUpdateEvent evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == ProgressUpdateEvent.ProgressUpdateEventListener.class) {
                ((ProgressUpdateEvent.ProgressUpdateEventListener) listeners[i + 1]).ProgressUpdateEventOccurred(evt);
            }
        }
    }
    private GUIInputStore in;

    private int lengthOfDays;
    private int done = 0;

    private final ExecutorService pool;

    public OverAllExecutor(GUIInputStore in) throws SQLException {
        this.in = in;

        //create results table
        Connection conn = DatabaseWorker.getConnection();
        conn.createStatement().execute("DROP TABLE IF EXISTS " + in.getResultTable());
        conn.createStatement().execute("CREATE TABLE " + in.getResultTable() + " (date1 int, grid1 int, val1 double,  "
                + "Primary key(date1, grid1) )");
        //
        DatabaseWorker.returnConnection(conn);
        lengthOfDays = in.getDates().size();
        pool = Executors.newFixedThreadPool(in.getNumberOfThreads());
        futures = new ArrayList();
    }
    private List<Future> futures;

    @Override
    protected Void doInBackground() throws Exception {
        fireProgressUpdateEvent(new ProgressUpdateEvent(this, -1));
        DistanceStore ds = new DistanceStore(in.getIdc());
        System.out.println("Adding to pool");
        for (Long date : in.getDates()) {
            OneDayExecutor O = new OneDayExecutor(date, ds, in.getRainTable(), in.getResultTable(), new IDWInterpolator(in.getPower()), in.getGrids());
            futures.add(pool.submit(O));

        }
        System.out.println("done to pool");
        while (!futures.isEmpty()) {
            List<Future> finished = new ArrayList();
            for (Future future : futures) {
                if (future.isDone()) {
                    publish((OneDayExecutor) future.get());
                    finished.add(future);
                }
            }
            futures.removeAll(finished);
        }
        return null;
    }

    @Override
    protected void done() {
        fireProgressUpdateEvent(new ProgressUpdateEvent(this, 101));
    }

    @Override
    protected void process(List<OneDayExecutor> chunks) {
        done = done + chunks.size();
        //    System.out.println("Update:" + (done * 100) / lengthOfDays);
        fireProgressUpdateEvent(new ProgressUpdateEvent(this, (done * 100) / lengthOfDays));
    }

}
