# MMO Resource Calculator

This is a simple WPF application for Windows that allows you to create tables of resources for MMO games. You can dynamically add columns and rows, attach images to cells, and save or load the table as XML.

## Features

- Add custom columns
- Add rows for resources
- Attach images to cells via double-click
- Save and load tables as XML files

To build the project, open `MMOResourceCalculator.csproj` in Visual Studio 2022 or later with .NET 8.0 SDK installed.

## StoreApp (Android)

`StoreApp` is a minimal Android application written in Kotlin to demonstrate a local authentication screen and a simple product list. Products can be searched and added on device only. This project is located in the `StoreApp` folder and can be opened with Android Studio.

Features:

- Local username/password storage using `SharedPreferences` (for demo only)
- Product list with search and ability to buy items while stock is available
- Adding new products with validation of price and stock

The application uses standard Android SDK components and does not require any backend.
