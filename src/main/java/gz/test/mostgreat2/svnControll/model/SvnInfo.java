package gz.test.mostgreat2.svnControll.model;

import java.util.ArrayList;
import java.util.List;

public class SvnInfo {
	
    private String name;
    private String path;
    private String author;
    private Long revision;
    private String date;
    
    private boolean open;
    private List<SvnInfo> subTree = new ArrayList<SvnInfo>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Long getRevision() {
		return revision;
	}
	public void setRevision(Long revision) {
		this.revision = revision;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public List<SvnInfo> getSubTree() {
		return subTree;
	}
	public void setSubTree(List<SvnInfo> subTree) {
		this.subTree = subTree;
	}
}
