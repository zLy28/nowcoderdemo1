package com.louie.nowcoderdemo1.entity;
/*
Serve page function
 */
public class Page {
    private int current = 1;
    private int numOfPostPerPage = 10;
    private int totalNumOfPost;
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getNumOfPostPerPage() {
        return numOfPostPerPage;
    }

    public void setNumOfPostPerPage(int numOfPostPerPage) {
        if (numOfPostPerPage>=1 && numOfPostPerPage <=50) {
            this.numOfPostPerPage = numOfPostPerPage;
        }
    }

    public int getTotalNumOfPost() {
        return totalNumOfPost;
    }

    public void setTotalNumOfPost(int totalNumOfPost) {
        if (totalNumOfPost>=0) {
            this.totalNumOfPost = totalNumOfPost;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getOffset() {
        return current * numOfPostPerPage - numOfPostPerPage;
    }

    public int getPageSum() {
        if (totalNumOfPost % numOfPostPerPage == 0) {
            return totalNumOfPost / numOfPostPerPage;
        } else {
            return (totalNumOfPost / numOfPostPerPage) + 1;
        }
    }

    public int getStartPage() {
        if (current - 3 < 1) {
            return 1;
        } else {
            return current - 3;
        }
    }

    public int getEndPage() {
        if (current + 3 > getPageSum()) {
            return getPageSum();
        } else {
            return current + 3;
        }
    }
}
