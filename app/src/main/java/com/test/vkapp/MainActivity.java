package com.test.vkapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.util.VKUtil;

import java.util.Arrays;

public class MainActivity extends Activity {
    // Указываем что хотим получать в приложение от пользователя
    private  String[] scope = new String[]{VKScope.MESSAGES,VKScope.FRIENDS,VKScope.WALL,VKScope.AUDIO};
    // Создание переменной для ListView(Область в которую выводить)
    private ListView listView;
    private Button showMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VKSdk.login(this,scope);   // Т.к. Наследуемся от Activity, пишем this


        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        System.out.println(Arrays.asList(fingerprints));  // Сертификат для получения sdk Vk для андроида
    }
    //Скопировано из сайта VK app dev
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
// Пользователь успешно авторизовался
                Toast.makeText(getApplicationContext(),"Вы успешно авторизированы!",Toast.LENGTH_LONG).show();
                //Создаем запрос на получение друзей
                listView = (ListView) findViewById(R.id.listView);
                //За запросы в ВК отвечает VKrequest. Получение имени и фамилии
                VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS,"frist_name,last_name)"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        // Можно еще использовать Vkusers array
                        VKList list = (VKList) response.parsedModel;
                        //Что бы положить лист в ListView необходимо:
                        ArrayAdapter<String> arraysAdapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_expandable_list_item_1,list);
                        //Получение списка пользователя для конкретного человека.
                        listView.setAdapter(arraysAdapter);
                    }
                });
            }
            @Override
            public void onError(VKError error) {
// Произошла ошибка авторизации (например, пользователь запретил авторизацию)
                Toast.makeText(getApplicationContext(),"Проблемы с авторизацией!",Toast.LENGTH_LONG).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    //Скопировано из сайта VK app dev
}
