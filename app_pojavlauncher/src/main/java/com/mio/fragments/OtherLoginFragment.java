package com.mio.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.kdt.mcgui.MineButton;
import com.kdt.mcgui.MineEditText;
import com.mio.login.AuthResult;
import com.mio.login.MioLoginApi;
import com.mio.login.Servers;

import net.kdt.pojavlaunch.PojavApplication;
import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.Tools;
import net.kdt.pojavlaunch.extra.ExtraConstants;
import net.kdt.pojavlaunch.extra.ExtraCore;
import net.kdt.pojavlaunch.fragments.MainMenuFragment;
import net.kdt.pojavlaunch.value.MinecraftAccount;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OtherLoginFragment extends Fragment {
    public static final String TAG = "OtherLoginFragment";
    private ProgressDialog progressDialog;
    private Spinner serverSpinner;
    private MineEditText userEditText;
    private MineEditText passEditText;
    private MineButton loginButton;
    private TextView register;
    private ImageButton addServer;
    private File serversFile;
    private Servers servers;
    private List<String> serverList;
    public String currentBaseUrl;
    private String currentRegisterUrl;
    private ArrayAdapter<String> serverSpinnerAdapter;


    public OtherLoginFragment() {
        super(R.layout.fragment_mio_plus_other_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        serversFile = new File(Tools.DIR_GAME_HOME, "servers.json");
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        serverSpinner = view.findViewById(R.id.server_spinner);
        userEditText = view.findViewById(R.id.login_edit_email);
        passEditText = view.findViewById(R.id.login_edit_password);
        loginButton = view.findViewById(R.id.login_button);
        register = view.findViewById(R.id.register);
        addServer = view.findViewById(R.id.add_server);

        refreshServer();
        serverSpinner.setAdapter(serverSpinnerAdapter);
        serverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!Objects.isNull(servers)) {
                    for (Servers.Server server : servers.getServer()) {
                        if (server.getServerName().equals(serverList.get(i))) {
                            currentBaseUrl = server.getBaseUrl();
                            currentRegisterUrl = server.getRegister();
                            Log.e("测试", "currentRegisterUrl:" + currentRegisterUrl);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        addServer.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(requireContext())
                    .setTitle("请选择认证服务器类型")
                    .setItems(new String[]{"外置登录", "统一通行证"}, (d, i) -> {
                        EditText editText = new EditText(requireContext());
                        editText.setMaxLines(1);
                        editText.setInputType(InputType.TYPE_CLASS_TEXT);
                        AlertDialog dialog1 = new AlertDialog.Builder(requireContext())
                                .setTitle("提示")
                                .setView(editText)
                                .setPositiveButton("确定", (dialogInterface, i1) -> {
                                    progressDialog.show();
                                    PojavApplication.sExecutorService.execute(() -> {
                                        String data = MioLoginApi.getINSTANCE().getServeInfo(i==0?editText.getText().toString():"https://auth.mc-user.com:233/" + editText.getText().toString());
                                        requireActivity().runOnUiThread(() -> {
                                            progressDialog.dismiss();
                                            if (!Objects.isNull(data)) {
                                                try {
                                                    Servers.Server server = new Servers.Server();
                                                    JSONObject jsonObject = new JSONObject(data);
                                                    JSONObject meta = jsonObject.optJSONObject("meta");
                                                    server.setServerName(meta.optString("serverName"));
                                                    server.setBaseUrl(editText.getText().toString());
                                                    if (i == 0) {
                                                        JSONObject links = meta.optJSONObject("links");
                                                        server.setRegister(links.optString("register"));
                                                    } else {
                                                        server.setBaseUrl("https://auth.mc-user.com:233/" + editText.getText().toString());
                                                        server.setRegister("https://login.mc-user.com:233/" + editText.getText().toString() + "/loginreg");
                                                    }
                                                    if (Objects.isNull(servers)) {
                                                        servers = new Servers();
                                                        servers.setInfo("Made by ShirosakiMio");
                                                        servers.setServer(new ArrayList<>());
                                                    }
                                                    servers.getServer().add(server);
                                                    Tools.write(serversFile.getAbsolutePath(), Tools.GLOBAL_GSON.toJson(servers, Servers.class));
                                                    refreshServer();
                                                    currentBaseUrl = server.getBaseUrl();
                                                    currentRegisterUrl = server.getRegister();
                                                } catch (Exception e) {
                                                    Log.e("测试", e.toString());
                                                }
                                            }
                                        });
                                    });

                                })
                                .setNegativeButton("取消", null)
                                .create();
                        if (i == 0) {
                            editText.setHint("请输入Yggdrasil API 认证服务器地址");
                        } else {
                            editText.setHint("请输入32位服务器ID");
                        }
                        dialog1.show();
                    })
                    .setNegativeButton("取消", null)
                    .create();
            dialog.show();
        });
        register.setOnClickListener(v -> {
            if (!Objects.isNull(currentRegisterUrl)) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri url = Uri.parse(currentRegisterUrl);
                intent.setData(url);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(v->{
            progressDialog.show();
            PojavApplication.sExecutorService.execute(()->{
                String user=userEditText.getText().toString();
                String pass=passEditText.getText().toString();
                if (!user.equals("")&&!pass.equals("")){
                    try {
                        MioLoginApi.getINSTANCE().setBaseUrl(currentBaseUrl);
                        MioLoginApi.getINSTANCE().login(user, pass, new MioLoginApi.Listener() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                requireActivity().runOnUiThread(()->{
                                    progressDialog.dismiss();
                                    MinecraftAccount account=new MinecraftAccount();
                                    account.accessToken=authResult.getAccessToken();
                                    account.baseUrl= currentBaseUrl;
                                    account.account=userEditText.getText().toString();
                                    account.password=passEditText.getText().toString();
                                    account.expiresAt=System.currentTimeMillis()+30*60*1000;
                                    if (!Objects.isNull(authResult.getSelectedProfile())){
                                        account.username=authResult.getSelectedProfile().getName();
                                        account.profileId=authResult.getSelectedProfile().getId();
                                        ExtraCore.setValue(ExtraConstants.OTHER_LOGIN_TODO, account);
                                        Tools.swapFragment(requireActivity(), MainMenuFragment.class, MainMenuFragment.TAG, false, null);
                                    } else {
                                        List<String> list=new ArrayList<>();
                                        for(AuthResult.AvailableProfiles profiles:authResult.getAvailableProfiles()){
                                            list.add(profiles.getName());
                                        }
                                        String[] items=list.toArray(new String[0]);
                                        AlertDialog dialog=new AlertDialog.Builder(requireContext())
                                                .setTitle("请选择角色")
                                                .setItems(items,(d,i)->{
                                                    for(AuthResult.AvailableProfiles profiles:authResult.getAvailableProfiles()){
                                                        if(profiles.getName().equals(items[i])){
                                                            account.profileId=profiles.getId();
                                                            account.username=profiles.getName();
                                                        }
                                                    }
                                                    ExtraCore.setValue(ExtraConstants.OTHER_LOGIN_TODO, account);
                                                    Tools.swapFragment(requireActivity(), MainMenuFragment.class, MainMenuFragment.TAG, false, null);
                                                })
                                                .setNegativeButton("取消",null)
                                                .create();
                                        dialog.show();
                                    }
                                });
                            }

                            @Override
                            public void onFailed(String error) {
                                requireActivity().runOnUiThread(()->{
                                    progressDialog.dismiss();
                                    AlertDialog dialog=new AlertDialog.Builder(requireContext())
                                            .setTitle("警告")
                                            .setTitle("登录时发生了错误：\n"+error)
                                            .setPositiveButton("确定",null)
                                            .create();
                                    dialog.show();
                                });
                            }
                        });
                    } catch (IOException e) {
                        requireActivity().runOnUiThread(()->progressDialog.dismiss());
                        Log.e("登录",e.toString());
                    }
                }
            });
        });
    }

    public void refreshServer() {
        if (Objects.isNull(serverList)) {
            serverList = new ArrayList<>();
        } else {
            serverList.clear();
        }
        if (serversFile.exists()) {
            try {
                servers = new Gson().fromJson(Tools.read(serversFile.getAbsolutePath()), Servers.class);
                currentBaseUrl=servers.getServer().get(0).getBaseUrl();
                for (Servers.Server server : servers.getServer()) {
                    serverList.add(server.getServerName());
                }
            } catch (IOException e) {

            }
        }
        if (Objects.isNull(servers)) {
            serverList.add("无认证服务器");
        }
        if (Objects.isNull(serverSpinnerAdapter)) {
            serverSpinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, serverList);
        } else {
            serverSpinnerAdapter.notifyDataSetChanged();
        }

    }
}
