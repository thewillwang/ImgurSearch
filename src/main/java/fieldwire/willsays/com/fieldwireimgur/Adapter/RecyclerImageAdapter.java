package fieldwire.willsays.com.fieldwireimgur.Adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import fieldwire.willsays.com.fieldwireimgur.AlertsFragment;
import fieldwire.willsays.com.fieldwireimgur.Data.Image;
import fieldwire.willsays.com.fieldwireimgur.R;
import fieldwire.willsays.com.fieldwireimgur.Util.RecyclerOnItemClickListener;
import fieldwire.willsays.com.fieldwireimgur.Util.SetText;

/**
 * Created by jingweiwang on 11/28/15.
 */
public class RecyclerImageAdapter extends RecyclerView.Adapter<RecyclerImageAdapter.ImageVH> implements RecyclerOnItemClickListener {
    private static List<Image> data;
    private static Context mContext;


    public RecyclerImageAdapter(List<Image> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
    }

    @Override
    public ImageVH onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_image, parent, false);
        return new ImageVH(view, this);
    }

    @Override
    public void onBindViewHolder(ImageVH holder, final int position) {
        Image person = data.get(position);
        holder.position = position;
        SetText.setString(holder.imageName, data.get(position).getName());
        SetText.setString(holder.imageDiscription, data.get(position).getDescription());

        holder.imageHolder.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.loading));


        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(data.get(position).getUrl(), holder.imageHolder);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onItemClicked(View view, int position) {
        Toast.makeText(view.getContext(), "Clicked position: " + position, Toast.LENGTH_SHORT).show();
        this.data.remove(position);
        notifyItemRemoved(position);
    }

    public void add(List<Image> items) {
        int previousDataSize = this.data.size();
        this.data.addAll(items);
        notifyItemRangeInserted(previousDataSize, items.size());
    }


    public static class ImageVH extends RecyclerView.ViewHolder {
        TextView imageName;
        TextView imageDiscription;
        ImageView imageHolder;
        int position;

        public ImageVH(View view, final RecyclerOnItemClickListener recyclerOnItemClickListener) {
            super(view);
            this.imageName = (TextView) view.findViewById(R.id.imageName);
            this.imageDiscription = (TextView) view.findViewById(R.id.imageDiscription);
            this.imageHolder = (ImageView) view.findViewById(R.id.imageHolder);

            Image img= data.get(position);
            if (img != null) {
                imageName.setText(img.getName());
                imageDiscription.setText(img.getDescription());
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(img.getUrl(), imageHolder);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.support.v4.app.FragmentManager fm = ((AppCompatActivity)mContext) .getSupportFragmentManager();
                    AlertsFragment alert = new AlertsFragment(data.get(position));
                    alert.show(fm,"showImage");
                }
            });
        }
    }

}
