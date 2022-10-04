# iTunesSearch

# iTunes search mobile app for Android:

# Important external libraries used in this project:

- Hilt: it is a dependency injection in Android. It allows the creation of dependent objects outside
  of a class and provides those objects to a class through different ways. It enables more code reuse,
  facilitates refactoring and testing.
    - @Module and @Provides: define classes and methods which provide dependencies.
    - @Inject: request dependencies. Can be used on a constructor, a field, or a method.

- kotlinx.coroutines: it is a rich library for coroutines developed by JetBrains. It contains a number of
  high-level coroutine-enabled primitives that this guide covers, including launch, async and others.
    - Flow: is a type that can emit multiple values sequentially, as opposed to suspend functions that
      return only a single value.

- Retrofit: it is a type-safe REST client for Android.

- Mockk: builds proxies for mocked classes for testing.

# Important architecture patterns used in this project:

- MVVM - stands for Model, View, ViewModel.
    - Model: holds the data of the application. In the project it exposes the data to the ViewModel
      through the repository.
    - View: It represents the UI of the application devoid of any Application Logic. It observes the
      ViewModel.
    - ViewModel: it is responsible for transforming the data from the Model