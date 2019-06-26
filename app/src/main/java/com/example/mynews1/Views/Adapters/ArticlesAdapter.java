package com.example.mynews1.Views.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.mynews1.Models.ArticleSearch;
import com.example.mynews1.Models.NYTimesNews;
import com.example.mynews1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder> {

    // FOR DATA
    private Picasso picasso;
    private List<NYTimesNews> mResultList;
    protected List<ArticleSearch.Doc> mArticleSearchesList;
    protected int mSearchConstructor;


    // CONSTRUCTOR
    public ArticlesAdapter(List<NYTimesNews> resultList, Picasso picasso) {
        this.mResultList = resultList;
        this.picasso = picasso;
    }

    public ArticlesAdapter(List<ArticleSearch.Doc> articleSearchesList, Picasso picasso, int searchConstructor) {
        this.mArticleSearchesList = articleSearchesList;
        this.picasso = picasso;
        this.mSearchConstructor = searchConstructor;
    }

    @Override
    public ArticlesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_item, parent, false);

        return new ArticlesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlesViewHolder holder, int position) {
            holder.updateWithArticles(mResultList.get(position), picasso);
    }
    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
            return mResultList.size();
    }

    public String getTitle(int position) {
            return mResultList.get(position).getTitle();
    }

    public String getURL(int position) {
            return mResultList.get(position).getUrl();
    }

    public void clearNews() {
        mResultList.clear();
        notifyDataSetChanged();
    }

    class ArticlesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_title)
        TextView mTextViewTitle;
        @BindView(R.id.item_category)
        TextView mTextViewSection;
        @BindView(R.id.item_date)
        TextView mTextViewDate;
        @BindView(R.id.item_image)
        ImageView mImageView;

        public ArticlesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void updateWithArticles(NYTimesNews article, Picasso picasso) {

            mTextViewSection.setText(article.getSection());
            mTextViewTitle.setText(article.getTitle());
            mTextViewDate.setText(article.getPublishedDate());
            updateImageView(article, picasso);
        }

        private void updateImageView(NYTimesNews result, Picasso picasso) {
            picasso.load(result.getImageURL())
                    .error(R.drawable.ic_launcher_background)
                    .into(mImageView);
        }
    }
}
