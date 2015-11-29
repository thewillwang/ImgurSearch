package fieldwire.willsays.com.fieldwireimgur;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import fieldwire.willsays.com.fieldwireimgur.Data.Image;
import fieldwire.willsays.com.fieldwireimgur.Util.SetText;

/**
 * Created by jingweiwang on 11/28/15.
 */
public class AlertsFragment extends DialogFragment {
    TextView imageName;
    TextView imageDiscription;
    ImageView imageHolder;
    private Image img;
    private EditText mEditText;

    public AlertsFragment(Image img) {
        this.img = img;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_fragment, container);

        this.imageName = (TextView) view.findViewById(R.id.imageName);
        this.imageDiscription = (TextView) view.findViewById(R.id.imageDiscription);
        this.imageHolder = (ImageView) view.findViewById(R.id.imageHolder);
        if (img != null) {
            SetText.setString(imageName, img.getName());
            SetText.setString(imageDiscription, img.getDescription());
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(img.getUrl(), imageHolder);
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertsFragment.this.dismiss();
            }
        });

        return view;
    }
}

