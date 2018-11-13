package dev.saber.com.ebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;
import java.io.File;
import java.util.List;


public class PdfActivity extends AppCompatActivity implements OnPageChangeListener,OnLoadCompleteListener {
    PDFView pdfView;


    private Context context;
    private SQLiteDatabase bookmarks;
    private final String databaseName = "bookmarks";
    private final int access = 1;

    Integer pageNumber = 0;
    String pdfFileName;
    String TAG="PdfActivity";
    int position=-1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        init();
    }
    private void init(){
        pdfView= (PDFView)findViewById(R.id.pdfView);
        position = getIntent().getIntExtra("position",-1);
        displayFromSdcard();
    }
    private void displayFromSdcard() {
        pdfFileName = MainActivity.fileList.get(position).getName();



        pdfView.fromFile(MainActivity.fileList.get(position))
                .defaultPage(pageNumber)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }
    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }
    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }
    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {
            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));
            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    public void bookmarked(View view){
       pdfFileName = MainActivity.fileList.get(position).getName();
       DatabaseHelper db = null;
       db.check(pdfFileName);



       //int y = db.check(pdfFileName);

      // this.bookmarks = context.openOrCreateDatabase(databaseName,2,null)

       //bookmarks.execSQL(createTable);
       //this.bookmarks.execSQL(createTable);

        //ContentValues values = new ContentValues();
        //values.put("name",pdfFileName);
        //values.put("page",pageNumber+1);
        //this.bookmarks.insert("bookmarks",null,values);

        Toast.makeText(getApplicationContext(), "Bookmarked "+pdfFileName+" on page "+(pageNumber+1)+" !", Toast.LENGTH_LONG).show();
    }
}