package laboratory.dxy.jack.com.textureviewdome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CoverFlowAdapter extends BaseAdapter {

    private ArrayList<GameEntity> mData = new ArrayList<>();
    private Context mContext;

    public CoverFlowAdapter(Context context) {
        mContext = context;

        mData.clear();
        mData.add(new GameEntity(R.drawable.item_background, "1"));
        mData.add(new GameEntity(R.drawable.item_background, "2"));
        mData.add(new GameEntity(R.drawable.item_background, "3"));
        mData.add(new GameEntity(R.drawable.item_background, "4"));

    }

    public void setData(ArrayList<GameEntity> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int pos) {
        return mData.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        ViewHolder viewHolder;

        if (rowView == null) {
//            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            rowView = LayoutInflater.inflate(R.layout.item_coverflow, null);

            rowView = LayoutInflater.from(mContext).inflate(R.layout.item_coverflow, null);

            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) rowView.findViewById(R.id.label);
            viewHolder.image = (ImageView) rowView
                    .findViewById(R.id.image);
            rowView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) rowView.getTag();
        }


//        holder.image.setBackground(mContext.getResources().getDrawable(R.drawable.image_1));
        viewHolder.image.setImageResource(R.drawable.image_1);
        viewHolder.text.setText(mData.get(position).titleResId);

        return rowView;
    }


    public class ViewHolder {
        public TextView text;
        public ImageView image;
    }


    public class GameEntity {
        public int imageResId;
        public String titleResId;

        public GameEntity(int imageResId, String titleResId) {
            this.imageResId = imageResId;
            this.titleResId = titleResId;
        }
    }
}