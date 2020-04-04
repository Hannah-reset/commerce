package cn.tedu.store.controller.ex;

/**
 * 上传文件的异常的基类
 */
public class FileUploadException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7687565953566383410L;

	public FileUploadException() {
		super();
	}

	public FileUploadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FileUploadException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileUploadException(String message) {
		super(message);
	}

	public FileUploadException(Throwable cause) {
		super(cause);
	}

}
