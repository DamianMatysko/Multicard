package sk.itsovy.multicard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;

public class CardsFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;

    static String[] titles = new String[]{};
    static String[] mDescription = new String[]{};
    static int[] images = new int[]{};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_cards, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();


//          String titles[] = {"Facebook", "Instagram", "Youtube", "Linkedin", "Reddit", "Tumblr", "Twitter", "Snapchat"};
//          String mDescription[] = {"Facebook Description", "Whatsapp Description", "Twitter Description", "Instagram Description", "Youtube Description", "Youtube Description", "Youtube Description", "Youtube Description"};
//          int images[] = {R.drawable.facebook, R.drawable.reddit, R.drawable.twitter, R.drawable.youtube, R.drawable.snapchat, R.drawable.tumblr, R.drawable.instagram, R.drawable.linkedin};


//        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID).collection("cards").);
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//
//                titles = increaseArray(titles, documentSnapshot.getString("accountType"));
//                mDescription = increaseArray(titles, documentSnapshot.getString("link"));
//               // images = addImage(images, getResources().getIdentifier("@drawable/"+documentSnapshot.getString("accountType").toLowerCase(), "drawable",  getActivity().getPackageName()));
//                images = addImage(images, R.drawable.facebook);
//                System.out.println(titles+"  ==  "+mDescription+"  ==  "+images);
//                System.out.println(documentSnapshot.getString("accountType")+"sssss"+documentSnapshot.getString("link"));
//            }
//        });


        firebaseFirestore.collection("users").document(userID).collection("cards")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                titles = increaseArray(titles, document.get("accountType").toString());
                                mDescription = increaseArray(mDescription, document.get("link").toString());
                                images = addImage(images, R.drawable.facebook);
                            }
//                            for (int i = 0;i < titles.length;i++){
//                                System.out.println("++++++++++++++++++++++++++");
//                                System.out.println(titles[i]);
//                                System.out.println(mDescription[i]);
//                                System.out.println(images[i]);
//                                System.out.println("\n\n\n\n\n\n");
//                            }


                        } else {
                            Toast.makeText(getActivity(), "Error: ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


//        DocumentReference docRef = firebaseFirestore.collection("users").document(userID);
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Toast.makeText(getActivity(), "Document exist", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getActivity(), "Document does not exist", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(getActivity(), "Error: ", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


        ListView listView = v.findViewById(R.id.listViewCards);

        System.out.println("aejdfhsadjfhajsdhfbajiskbfaiskjhdbaosifbnwSDIPAFBWPSEDFIUBWRPEDUFVGBWERUIDGB");
        for (int i = 0; i < titles.length; i++) {
            System.out.println("++++++++++++++++++++++++++");
            System.out.println(titles[i]);
            System.out.println(mDescription[i]);
            System.out.println(images[i]);
            System.out.println("\n\n\n\n\n\n");
        }

        MyAdapter adapter = new MyAdapter(getContext(), titles, mDescription, images);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(parent.get));
                startActivity(browserIntent);
            }
        });
        return v;
    }


    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String rTitle[];
        String rDescription[];
        int rImgs[];

        MyAdapter(Context c, String title[], String description[], int imgs[]) {
            super(c, R.layout.card_row, R.id.textView, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
            this.rImgs = imgs;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.card_row, parent, false);
            ImageView images = row.findViewById(R.id.image1);
            TextView myTitle = row.findViewById(R.id.title1);
            TextView myDescription = row.findViewById(R.id.title2);

            images.setImageResource(rImgs[position]);
            myTitle.setText(rTitle[position]);
            myDescription.setText(rDescription[position]);

            return row;
        }
    }

    public String[] increaseArray(String[] theArray, String newValue) {
        int i = theArray.length;
        String[] newArray = new String[++i];
        for (int cnt = 0; cnt < theArray.length; cnt++) {
            newArray[cnt] = theArray[cnt];
        }
        newArray[theArray.length] = newValue;
        return newArray;
    }

    public int[] addImage(int[] theArray, int newInt) {
        int i = theArray.length;
        int[] newArray = new int[++i];
        for (int cnt = 0; cnt < theArray.length; cnt++) {
            newArray[cnt] = theArray[cnt];
        }
        newArray[theArray.length] = newInt;
        return newArray;
    }
}