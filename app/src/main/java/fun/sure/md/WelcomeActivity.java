package fun.sure.md;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Palette.PaletteAsyncListener;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WelcomeActivity extends AppCompatActivity {

    private CoordinatorLayout layoutContainer;
    private FloatingActionButton fabView;
    private CardView cardView;
    private RecyclerView recyclerView;

    // data
    private List<String> listData;
    private InnerAdapter adapter;

    private static int[] resIds = {
            R.mipmap.image_1,
            R.mipmap.image_2,
            R.mipmap.image_3,
            R.mipmap.image_4,
            R.mipmap.image_5
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
    }

    private void initView() {
        layoutContainer = (CoordinatorLayout) findViewById(R.id.container);
        fabView = (FloatingActionButton) findViewById(R.id.fab);
        cardView = (CardView) findViewById(R.id.card);
        recyclerView = (RecyclerView) findViewById(R.id.list);

        fabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRandomIndex() % 2 == 0) {
                    adapter.addData(1);
                } else {
                    adapter.removeData(1);
                }
            }
        });

        recyclerViewDemeo();
    }

    private int getRandomIndex() {
        return new Random().nextInt(resIds.length);
    }

    private void generatePalette(int i) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resIds[i]);
        if (bitmap != null) {
            Palette.from(bitmap).generate(new PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    fabView.setBackgroundColor(palette.getLightVibrantColor(android.R.color.transparent));
                }
            });
        }
    }

    private List<String> getListData() {
        if (listData == null) {
            listData = new ArrayList<>();
            for (int i = 'A'; i <= 'Z'; i++) {
                listData.add(String.valueOf((char) i));
            }
        }
        return listData;
    }

    /**
     * Recycler View Demo
     */
    private void recyclerViewDemeo() {
        adapter = new InnerAdapter();
//        Linear
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    //region inner class

    private class InnerAdapter extends RecyclerView.Adapter<InnerAdapter.InnerViewHolder> {

        @Override
        public InnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(WelcomeActivity.this).inflate(R.layout.view_recycler_item, parent, false);
            InnerViewHolder viewHolder = new InnerViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(InnerViewHolder holder, final int position) {
            holder.textView.setText(getListData().get(position));
            holder.textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(layoutContainer, getListData().get(position), Snackbar.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return getListData().size();
        }

        public void addData(int position) {
            getListData().add(position, "insert");
            notifyItemInserted(position);
        }

        public void removeData(int position) {
            getListData().remove(position);
            notifyItemRemoved(position);
        }

        class InnerViewHolder extends ViewHolder {

            TextView textView;

            public InnerViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.text);
            }
        }
    }

    //endregion
}
