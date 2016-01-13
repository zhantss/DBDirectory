package pro.zhantss.lucene.store.database.lock;

import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.Lock;
import org.apache.lucene.store.LockFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.zhantss.lucene.store.database.DatabaseDirectory;
import pro.zhantss.lucene.store.database.handler.DatabaseDirectoryHandler;

@SuppressWarnings("unused")
public class DatabaseSelectForUpdateLockFactory extends LockFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseSelectForUpdateLockFactory.class);
    private static final DatabaseDirectoryHandler handler = DatabaseDirectoryHandler.INSTANCE;

    public static final LockFactory INSTANCE = new DatabaseSelectForUpdateLockFactory();

    private DatabaseSelectForUpdateLockFactory() {
    }

    public Lock obtainLock(final Directory dir, final String lockName) throws IOException {
        LOGGER.info("{}.obtainLock({}, {})", this, dir, lockName);
        // TODO Auto-generated method stub
        return null;

        // final DatabaseDirectory directory = (DatabaseDirectory) dir;
        // try {
        // handler.saveFile(directory, lockName, null, 0);
        // return new DatabaseSelectForUpdateLock(directory, lockName);
        // } catch (final DatabaseDirectoryException e) {
        // throw new LockObtainFailedException("Lock instance already obtained: " + directory);
        // }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    static final class DatabaseSelectForUpdateLock extends Lock {

		private final DatabaseDirectory directory;
        private final String name;
        private volatile boolean closed;

        public DatabaseSelectForUpdateLock(final DatabaseDirectory directory, final String name) {
            this.directory = directory;
            this.name = name;
        }

        @Override
        public void close() throws IOException {
            // TODO Auto-generated method stub

        }

        public void ensureValid() throws IOException {
            // TODO Auto-generated method stub

        }

		@Override
		public boolean obtain() throws IOException {
			// TODO 自动生成的方法存根
			return false;
		}

		@Override
		public boolean isLocked() throws IOException {
			// TODO 自动生成的方法存根
			return false;
		}

    }

	@Override
	public Lock makeLock(String lockName) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void clearLock(String lockName) throws IOException {
		// TODO 自动生成的方法存根
		
	}

}
