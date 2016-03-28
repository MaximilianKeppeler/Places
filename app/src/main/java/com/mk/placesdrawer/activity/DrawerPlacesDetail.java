package com.mk.placesdrawer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mk.placesdrawer.R;
import com.mk.placesdrawer.adapters.PlacesDetailAdapter;
import com.mk.placesdrawer.models.PlacesDetailItem;
import com.mk.placesdrawer.models.PlacesItem;
import com.mk.placesdrawer.widgets.SquareImageView;

import java.util.Arrays;

public class DrawerPlacesDetail extends AppCompatActivity {

    public static Toolbar toolbar;
    private Activity context;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fab;
    private TextView placeDescTitle, placesDescText, placeInfoTitle;
    private PlacesItem item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setNavigationBarColor(getResources().getColor(R.color.navigationBar));
            window.setStatusBarColor(getResources().getColor(R.color.colorStatusBarOverlay));
        }

        context = this;
        initActivityTransitions();
        setContentView(R.layout.drawer_places_detail);

        Intent intent = getIntent();
        item = intent.getParcelableExtra("item");

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/BreeSerif-Regular.ttf");

        final String itemImage = item.getImgPlaceUrl();
        final String itemLocation = item.getLocation();

//        Start of Details CardView / RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewInfoDetails);

        String position;
        String[] positionArray = {   item.getCountry(), item.getState(), item.getCity()    };
        position = Arrays.toString(positionArray).replace("[", "").replace("]", "").replace(",", "  ~ ");


        if (item.getSight().equals("City")) {
            PlacesDetailItem itemsData[] = {
                    new PlacesDetailItem("Location", position, R.drawable.ic_location),
                    new PlacesDetailItem("Religion", item.getReligion(), R.drawable.ic_religion),
            };
            finishRecycler(recyclerView, itemsData);
        }

        if (item.getSight().equals("National Park")) {
            PlacesDetailItem itemsData[] = {
                    new PlacesDetailItem("Location", position, R.drawable.ic_location),
            };
            finishRecycler(recyclerView, itemsData);
        }

        ViewCompat.setTransitionName(findViewById(R.id.appBarLayout), itemImage);
        supportPostponeEnterTransition();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        placeDescTitle = (TextView) findViewById(R.id.placeDescTitle);
        placeDescTitle.setTypeface(typeface);
        placeInfoTitle = (TextView) findViewById(R.id.placeInfoTitle);
        placeInfoTitle.setTypeface(typeface);

        placesDescText = (TextView) findViewById(R.id.descDetailView);
        placesDescText.setText(item.getDescription());

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Floating Action Button", "onClick: works ");

            }
        });

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitle(itemLocation);
        collapsingToolbarLayout.setCollapsedTitleTypeface(typeface);
        collapsingToolbarLayout.setExpandedTitleTypeface(typeface);

        final SquareImageView image = (SquareImageView) findViewById(R.id.image);

        Glide.with(context)
                .load(itemImage)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .skipMemoryCache(true)
                .centerCrop()
                .into(new BitmapImageViewTarget(image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), new BitmapDrawable(getResources(), resource)});
                        image.setImageDrawable(td);
                        td.startTransition(450);
                        new Palette.Builder(resource).generate(paletteAsyncListener);
                    }
                });
    }

    private void finishRecycler(RecyclerView recyclerView, PlacesDetailItem itemsData[]) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        PlacesDetailAdapter mAdapter = new PlacesDetailAdapter(itemsData);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        closeViewer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.toolbar_drawer_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                closeViewer();
                break;
            case R.id.share:
                share();
//                TODO
                break;
            case R.id.launch:
//                TODO
                break;
        }
        return true;
    }

    private void closeViewer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportFinishAfterTransition();
        } else {
            finish();
        }
    }

    public void share() {
//        TODO Fix
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_SUBJECT, "Places - " + item.getLocation());
        share.putExtra(Intent.EXTRA_TEXT, item.getDescription());
        share.putExtra(Intent.EXTRA_STREAM, item.getImgPlaceUrl());
        startActivity(Intent.createChooser(share, "Share Place"));
    }
    private int getAColor(Palette palette) {

        final int defaultColor = getResources().getColor(R.color.colorPrimary);
        int mutedDark = palette.getDarkMutedColor(defaultColor);
        int vibrantDark = palette.getDarkVibrantColor(mutedDark);
        int mutedLight = palette.getLightMutedColor(vibrantDark);
        int muted = palette.getMutedColor(mutedLight);
        int vibrantLight = palette.getLightVibrantColor(muted);
        return palette.getVibrantColor(vibrantLight);
    }

    public Palette.PaletteAsyncListener paletteAsyncListener =
            new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    if (palette == null) return;
                    int generatedColor = getAColor(palette);

                    fab.setRippleColor(generatedColor);
                    fab.setBackgroundTintList(ColorStateList.valueOf(generatedColor));
                    fab.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_up);
                    fab.startAnimation(animation);

                    collapsingToolbarLayout.setContentScrimColor(generatedColor);

                    if (generatedColor != getResources().getColor(R.color.colorPrimary)) {
                        collapsingToolbarLayout.setStatusBarScrimColor(generatedColor);
                    } else {
                        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimaryDark));
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.setNavigationBarColor(generatedColor);
                    }
                }
            };



}