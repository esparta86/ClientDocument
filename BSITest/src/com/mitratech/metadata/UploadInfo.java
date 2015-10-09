package com.mitratech.metadata;

public class UploadInfo {

    //! Temporary upload ID given by the server.
    private int UploadID;
    
    //! File name without extension.
    private String Title;
    
    //! File extension.
    private String Extension;
    
    public int getUploadID() {
		return UploadID;
	}

	public void setUploadID(int uploadID) {
		UploadID = uploadID;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getExtension() {
		return Extension;
	}

	public void setExtension(String extension) {
		Extension = extension;
	}

	//! File size.
    private long Size;
    
    public UploadInfo(int UploadID,String Title,String Extension,long Size){
     this.UploadID = UploadID;
     this.Title = Title;
     this.Extension = Extension;
     this.Size =Size;
    }

	public long getSize() {
		return Size;
	}

	public void setSize(long size) {
		Size = size;
	}
    
    
    
}
