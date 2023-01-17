package com.ukolov.s4etovod

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.ukolov.s4etovod.api.RetrofitService
import com.ukolov.s4etovod.databinding.ActivityMainBinding
import com.ukolov.s4etovod.model.login.LogPass
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val compositeDisposable = CompositeDisposable()

    lateinit var viewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Если необходимо подключаем логгирование запроса. ТОЛЬКО ДЛЯ ДЕБАГА!!!
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api-test.s4vod.ru")

            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
        //создаем реализацию нашего интерфейса с помощью ретрофит
        val messageApi = retrofit.create(RetrofitService::class.java)

        binding.bSignIn.setOnClickListener {
            val login = binding.edLogin.text.toString()
            val password = binding.edPassword.text.toString()
            if (login.isNotEmpty() && password.isNotEmpty()) {
                val loginPassword = LogPass(login, password)
                var disposable = messageApi.getToken(loginPassword)
                    .subscribeOn(Schedulers.io())//сделать в потоке ввода вывода
                    .observeOn(AndroidSchedulers.mainThread())//слушаем ответ в главном потоке
                    .subscribe({
                        Log.d("Token", it.token.toString())
                        viewModel.token?.value = it.token.toString()
                    }, {
                        Log.d("Token", "Ошибка получения токена")
                    })
                compositeDisposable.add(disposable) //помещаем запрос в контейнер одноразовых элементов

            } else {
                Log.d("Token", "Логин или пароль пустой")
            }
        }
        //-----------
        binding.bGetCounterType.setOnClickListener {

/*         Еще один метод передать токен авторизации
           val headers = HashMap<String, String>()
           headers["KEY_AUTHORIZATION"] = "paste AUTHORIZATION value here"
           headers["Authorization"] = "Bearer "+binding.tvToken.text.toString()
           var disposable = messageApi.getCounterType(headers) */
            var disposable =
                messageApi.getCounterType1("Bearer " + viewModel.token.value.toString())

                    .subscribeOn(Schedulers.io())//сделать в потоке ввода вывода
                    .observeOn(AndroidSchedulers.mainThread())//слушаем ответ в главном потоке
                    .subscribe({
                        Log.d("COUNTER_TYPE", it.toString())
                    }, {
                        Log.d("COUNTER_TYPE", "Ошибка получения типов счетчиков")
                    })
            compositeDisposable.add(disposable) //помещаем запрос в контейнер одноразовых элементов
        }

        //-----------
        binding.bGetRooms.setOnClickListener {
            var disposable = messageApi.getRooms("Bearer " + viewModel.token.value.toString())

                .subscribeOn(Schedulers.io())//сделать в потоке ввода вывода
                .observeOn(AndroidSchedulers.mainThread())//слушаем ответ в главном потоке
                .subscribe({
                    Log.d("GET ROOMS", it.toString())
                    binding.textView.text = it.toString()
                }, {
                    Log.d("GET ROOMS", "Ошибка получения данных")
                })
            compositeDisposable.add(disposable) //помещаем запрос в контейнер одноразовых элементов
        }


        //-----------
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        viewModel.token.observe(this, {
            binding.tvToken.text = it.toString()
            Log.d("VIEW_MODEL", "поменялся токен")
        })
    }

    override fun onDestroy() {
        compositeDisposable.dispose()  //очищаем контейнер чтобы запросы не занимали место
        super.onDestroy()
    }
}

