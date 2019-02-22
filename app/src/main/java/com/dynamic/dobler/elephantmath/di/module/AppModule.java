package com.dynamic.dobler.elephantmath.di.module;

import dagger.Module;

@Module(includes = ViewModelModule.class)
public class AppModule {

    // --- DATABASE INJECTION ---
//
//    @Provides
//    @Singleton
//    MyDatabase provideDatabase(Application application) {
//        return Room.databaseBuilder(application,
//                MyDatabase.class, "MyDatabase.db")
//                .build();
//    }
//
//    @Provides
//    @Singleton
//    UserDao provideUserDao(MyDatabase database) { return database.userDao(); }
//
//    // --- REPOSITORY INJECTION ---
//
//    @Provides
//    Executor provideExecutor() {
//        return Executors.newSingleThreadExecutor();
//    }
//
//    @Provides
//    @Singleton
//    UserRepository provideUserRepository(UserWebservice webservice, UserDao userDao, Executor executor) {
//        return new UserRepository(webservice, userDao, executor);
//    }
//
//    // --- NETWORK INJECTION ---
//
//    private static String BASE_URL = "https://api.github.com/";
//
//    @Provides
//    Gson provideGson() { return new GsonBuilder().create(); }
//
//    @Provides
//    Retrofit provideRetrofit(Gson gson) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .baseUrl(BASE_URL)
//                .build();
//        return retrofit;
//    }
//
//    @Provides
//    @Singleton
//    UserWebservice provideApiWebservice(Retrofit restAdapter) {
//        return restAdapter.create(UserWebservice.class);
//    }
}
