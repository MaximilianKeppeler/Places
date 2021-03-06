package com.earth.places.utilities;

import android.app.Activity;

import com.afollestad.inquiry.Inquiry;
import com.afollestad.inquiry.annotations.Column;

/**
 *  Thanks Aidan Follestad
 */
public final class Bookmarks {

    private static Activity context;

    public static void init(Activity ctxt) {
        context = ctxt;
        Inquiry.init(ctxt, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);
    }


    /**
     * Returns true if the item is currently favorited.
     */
    public static boolean isBookmarked(String id) {
        return Inquiry.get()
                .selectFrom(Constants.DATABASE_NAME, BookmarksDB.class)
                .where("_id = ?", id)
                .one() != null;
    }

    /**
     * Returns true if the item was favorited successfully.
     */
    public static boolean bookmark(String id) {

        init(context);

        if (!isBookmarked(id)) {
            Inquiry.get()
                    .insertInto(Constants.DATABASE_NAME, BookmarksDB.class)
                    .values(new BookmarksDB(id))
                    .run();
            return true;
        } else unbookmark(id);

        return false;
    }

    /**
     * Returns Array with all IDs
     */
    public static BookmarksDB[] getDB() {

        return Inquiry.get().selectFrom(Constants.DATABASE_NAME, BookmarksDB.class).all();
    }

    /**
     * Deletes Data Base
     */
    public static void deleteDB() {

        init(context);

        Inquiry.get().dropTable(Constants.DATABASE_NAME);

        Inquiry.deinit();
    }


    /**
     * Returns true if the item was unfavorited successfully.
     */
    public static boolean unbookmark(String id) {
        if (isBookmarked(id)) {
            Inquiry.get()
                    .deleteFrom(Constants.DATABASE_NAME, BookmarksDB.class)
                    .where("_id = ?", id)
                    .run();
            return true;
        } else bookmark(id);

        return false;
    }

    public static class BookmarksDB {

        @Column(name = "_id", primaryKey = true, notNull = true, autoIncrement = false)
        public String _id;

        public BookmarksDB() {
        }

        public BookmarksDB(String ID) {
            this._id = ID;
        }

        public String getID() {
            return _id;
        }
    }
}