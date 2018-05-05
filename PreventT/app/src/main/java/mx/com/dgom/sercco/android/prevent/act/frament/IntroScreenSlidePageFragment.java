package mx.com.dgom.sercco.android.prevent.act.frament;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import mx.com.dgom.sercco.android.prevent.act.R;

/**
 * Created by beto on 2/14/16.
 */
public class IntroScreenSlidePageFragment extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static IntroScreenSlidePageFragment create(int pageNumber) {
        IntroScreenSlidePageFragment fragment = new IntroScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public IntroScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_intro_screen_slide_page, container, false);

        //Oculta el boton
        ((Button) rootView.findViewById(R.id.btn_entiendo)).setVisibility(View.GONE);
        // Set the image
        switch (mPageNumber) {
            case 0:
                ((ImageView) rootView.findViewById(R.id.img_intro_page)).setImageResource(R.drawable.p_tut_pag_1);
                break;
            case 1:
                ((ImageView) rootView.findViewById(R.id.img_intro_page)).setImageResource(R.drawable.p_tut_pag_2);
                break;
            case 2:
                ((ImageView) rootView.findViewById(R.id.img_intro_page)).setImageResource(R.drawable.p_tut_pag_3);
                break;
            case 3:
                ((ImageView) rootView.findViewById(R.id.img_intro_page)).setImageResource(R.drawable.p_tut_pag_4);
                ((Button) rootView.findViewById(R.id.btn_entiendo)).setVisibility(View.VISIBLE);
                break;
        }


        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}

