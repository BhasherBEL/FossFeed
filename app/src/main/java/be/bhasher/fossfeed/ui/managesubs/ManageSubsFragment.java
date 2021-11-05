package be.bhasher.fossfeed.ui.managefeeds;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import be.bhasher.fossfeed.databinding.FragmentManagefeedsBinding;

public class ManageFeedsFragment extends Fragment {
    private FragmentManagefeedsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentManagefeedsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textManagefeeds;

        textView.setText("Manage your feeds");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}