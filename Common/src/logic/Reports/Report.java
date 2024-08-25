package logic.Reports;

import java.io.Serializable;

import EnumsAndConstants.BranchLocation;

public class Report implements Serializable {

    private static final long serialVersionUID = -5432585210792937987L;
    private String title;
    private String date;
    private BranchLocation branchLocation;

    public Report(String title, String date, BranchLocation branchLocation) {
        this.title = title;
        this.date = date;
        this.branchLocation = branchLocation;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public BranchLocation getBranchName() {
        return branchLocation;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setBranchName(BranchLocation branchLocation) {
        this.branchLocation = branchLocation;
    }
}
