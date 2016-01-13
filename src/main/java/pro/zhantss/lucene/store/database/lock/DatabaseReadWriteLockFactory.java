package pro.zhantss.lucene.store.database.lock;

import java.io.IOException;

import org.apache.lucene.store.AlreadyClosedException;
import org.apache.lucene.store.Lock;
import org.apache.lucene.store.LockFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.zhantss.lucene.store.database.DatabaseDirectory;
import pro.zhantss.lucene.store.database.handler.DatabaseDirectoryHandler;

public class DatabaseReadWriteLockFactory extends LockFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseReadWriteLockFactory.class);
    private static final DatabaseDirectoryHandler handler = DatabaseDirectoryHandler.INSTANCE;
    
    private DatabaseDirectory directory;

    public DatabaseReadWriteLockFactory(DatabaseDirectory directory) {
    	this.directory = directory;
    }

    private Lock obtainLock(final DatabaseDirectory dir, final String lockName) throws IOException {
        LOGGER.info("{}.obtainLock({}, {})", this, dir, lockName);
        /**
        try {
            if (handler.existsFile(directory, lockName)) {
                throw new LockObtainFailedException("Lock instance already obtained: " + directory);
            }
            handler.saveFile(directory, lockName, null, 0);
            return new DatabaseReadWriteLock(directory, lockName);
        } catch (final DatabaseDirectoryException e) {
            throw new LockObtainFailedException("Lock instance already obtained: " + directory);
        }
        */
        
        // TODO Lucene 4.7 加锁工作交由Lock完成
        return new DatabaseReadWriteLock(dir, lockName);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public static final class DatabaseReadWriteLock extends Lock {

        private final DatabaseDirectory directory;
        private final String name;
        private volatile boolean closed;

        public DatabaseReadWriteLock(final DatabaseDirectory directory, final String name) {
            this.directory = directory;
            this.name = name;
        }

        public void ensureValid() throws IOException {
            LOGGER.debug("{}.ensureValid()", this);
            if (closed) {
                throw new AlreadyClosedException("Lock instance already released: " + this);
            }
            if (!handler.existsFile(directory, name)) {
                throw new AlreadyClosedException("Lock instance already released: " + this);
            }
        }

        @Override
        public void close() throws IOException {
            LOGGER.debug("{}.close()", this);
            if (!closed) {
                handler.deleteFile(directory, name);
                closed = true;
            }
        }

        @Override
        public String toString() {
        	// TODO 由toString()在父类Lock的obtain中抛出LockObtainFailedException的reason
            return this.getClass().getSimpleName() + ": " + "Lock instance already obtained: " + directory;
        }

		@Override
		public boolean obtain() throws IOException {
			// TODO 测试
			if (handler.existsFile(directory, name)) {
				return false;
			}
			try {
				handler.saveFile(directory, name, null, 0);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		public boolean isLocked() throws IOException {
			return handler.existsFile(directory, name);
		}

    }

	@Override
	public Lock makeLock(String lockName) {
		try {
			return obtainLock(this.directory, lockName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void clearLock(String lockName) throws IOException {
		// TODO 测试
		if (handler.existsFile(directory, lockName)) {
			handler.deleteFile(directory, lockName);
		}
	}

}
