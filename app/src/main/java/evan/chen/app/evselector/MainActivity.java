package evan.chen.app.evselector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EVSelector selector;
    private TextView selectedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.selectedTextView = (TextView)this.findViewById(R.id.selected_text_view);

        this.selector = (EVSelector)this.findViewById(R.id.select_dialog);

        int[] drawables = {
                R.mipmap.icon1,
                R.mipmap.icon2,
                R.mipmap.icon3
        };

        this.selector.setSelectIcon(drawables);

        this.selector.setListener(new EVSelector.IconSelectListener() {
            @Override
            public void onOpen() {
                selectedTextView.setText("");
            }

            @Override
            public void onSelected(int iconIndex) {
                selectedTextView.setText("Select  icon: " + iconIndex);
            }

            @Override
            public void onCancel() {
                selectedTextView.setText("Cancel");
            }
        });
    }
}
