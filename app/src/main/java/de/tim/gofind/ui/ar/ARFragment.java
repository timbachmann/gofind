package de.tim.gofind.ui.ar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.tim.gofind.databinding.FragmentArBinding;

public class ARFragment extends Fragment {

    private ARViewModel ARViewModel;
    private FragmentArBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ARViewModel =
                new ViewModelProvider(this).get(ARViewModel.class);

        binding = FragmentArBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button arButton = binding.arcoreButton;
        arButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SharedCameraActivity.class);
                startActivity(intent);

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    @SuppressLint("RestrictedApi")
    public void onResume() {
        super.onResume();
        ActionBar supportActionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.setShowHideAnimationEnabled(false);
            supportActionBar.hide();
    }

    @Override
    @SuppressLint("RestrictedApi")
    public void onStop() {
        super.onStop();
        ActionBar supportActionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.setShowHideAnimationEnabled(false);
            supportActionBar.show();
    }
}