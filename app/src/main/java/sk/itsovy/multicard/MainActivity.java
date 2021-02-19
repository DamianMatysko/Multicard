package sk.itsovy.multicard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private Toolbar mToolbar;

    private EditText editAccountType;
    private EditText editLink;

    String userID;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).commit();
        }

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.app_bar_add:
                        Toast.makeText(MainActivity.this,"Add",Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(MainActivity.this, AddCards.class));
                       // finish();


                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                        View view = inflater.inflate(R.layout.add_dialog, null);
                        builder.setView(view)
                                .setTitle("ADD")
                                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                })
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String accountType = editAccountType.getText().toString();
                                        String link = editLink.getText().toString();

                                        userID = firebaseAuth.getCurrentUser().getUid();
                                        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID).collection("cards").document();

                                        Map<String, Object> user = new HashMap<>();
                                        user.put("accountType", accountType);
                                        user.put("link", link);
                                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "onSuccess: User card is created " + userID);
                                                Toast.makeText(MainActivity.this,"User card is created",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });

                        editAccountType = view.findViewById(R.id.edit_account_type);
                        editLink = view.findViewById(R.id.edit_link);
                        builder.show();

                        break;
                    case R.id.app_bar_logout:
                        Toast.makeText(MainActivity.this,"Logout",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, Login.class));
                        finish();
                    default:
                }
                return false;
            }
        });

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.profile:
//                            mToolbar.getMenu().getItem(R.id.app_bar_add).setVisible(false);
//                            mToolbar.getMenu().getItem(R.id.logoutButton).setVisible(true);
                            // appBarAdd.setVisible(false);
                            // appBarLogout.setVisible(true);
                            selectedFragment = new ProfileFragment();
                            break;
                        case R.id.cards:
                            // appBarAdd.setVisible(false);
                            //  appBarLogout.setVisible(true);
//                            mToolbar.getMenu().getItem(R.id.app_bar_add).setVisible(true);
//                            mToolbar.getMenu().getItem(R.id.logoutButton).setVisible(false);
                            selectedFragment = new CardsFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.top_bar, menu);
        return true;
    }
}

