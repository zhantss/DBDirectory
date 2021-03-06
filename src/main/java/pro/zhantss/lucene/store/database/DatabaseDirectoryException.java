package pro.zhantss.lucene.store.database;

import java.io.IOException;

public class DatabaseDirectoryException extends IOException {

    private static final long serialVersionUID = -2446717740777213051L;

    /**
     * @param cause
     */
    public DatabaseDirectoryException(final Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public DatabaseDirectoryException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
