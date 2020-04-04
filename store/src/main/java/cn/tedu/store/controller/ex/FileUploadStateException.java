package cn.tedu.store.controller.ex;

/**
 * 上传时文件状态异常，例如上传过程中源文件被移除，导致源文件不再被访问
 */
public class FileUploadStateException extends FileUploadException {

	private static final long serialVersionUID = -2508877005843296633L;

	public FileUploadStateException() {
		super();
	}

	public FileUploadStateException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FileUploadStateException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileUploadStateException(String message) {
		super(message);
	}

	public FileUploadStateException(Throwable cause) {
		super(cause);
	}

}
