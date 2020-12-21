package com.onlineinkpot.models;

/**
 * Created by Akshay on 10/4/2017.
 */

public class ModTestChapter {
    private String ChapterId,ChapterName;

    public ModTestChapter() {

    }

    public String getChapterId() {
        return ChapterId;
    }

    public void setChapterId(String chapterId) {
        ChapterId = chapterId;
    }

    public String getChapterName() {
        return ChapterName;
    }

    public void setChapterName(String chapterName) {
        ChapterName = chapterName;
    }

    public ModTestChapter(String chapterId, String chapterName) {
        ChapterId = chapterId;
        ChapterName = chapterName;
    }
}
