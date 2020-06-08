package com.loopedlabs.selector;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter used to display a files list
 */
public class FileListAdapter extends BaseAdapter {

    /**
     * Array of FileData objects that will be used to display a list
     */
    private final ArrayList<FileData> mFileDataArray;

    private final Context mContext;

    public FileListAdapter(Context context, List<FileData> aFileDataArray) {
        mFileDataArray = (ArrayList<FileData>) aFileDataArray;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mFileDataArray.size();
    }

    @Override
    public Object getItem(int position) {
        return mFileDataArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FileData tempFileData = mFileDataArray.get(position);
        TextViewWithImage tempView = new TextViewWithImage(mContext);
        tempView.setText(tempFileData.getFileName());
        int imgRes = -1;
        switch (tempFileData.getFileType()) {
            case FileData.UP_FOLDER:
                imgRes = R.mipmap.icon_up_folder;
                break;
            case FileData.DIRECTORY:
                imgRes = R.mipmap.icon_folder;
                break;
            case FileData.FILE:
                imgRes = R.mipmap.icon_unknown_file;
                break;
            case FileData.ZIP_FILE:
                imgRes = R.mipmap.icon_zip_file;
                break;
            case FileData.CSV_FILE:
                imgRes = R.mipmap.icon_csv_file;
                break;
            case FileData.HTML_FILE:
                imgRes = R.mipmap.icon_html_file;
                break;
            case FileData.PDF_FILE:
                imgRes = R.mipmap.icon_pdf_file;
                break;
            case FileData.PNG_FILE:
                imgRes = R.mipmap.icon_png_file;
                break;
            case FileData.JPG_FILE:
                imgRes = R.mipmap.icon_jpg_file;
                break;
        }
        tempView.setImageResource(imgRes);
        return tempView;
    }
}
