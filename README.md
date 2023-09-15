# AKL Live EV Bus

This is the official distribution page for the `AKL Live EV Bus` app. Please take a look at the information below on how to use it.

We support Auckland Transport's [Mission Electric](https://at.govt.nz/about-us/sustainability/mission-electric/).

## Features

- [x] A list to show all electric & new energy buses operating currently.
- [x] A map visually presenting all electric & new energy buses operating currently.
- [x] A neat Material 3 UI & Dark mode adapted.
- [ ] Detail page showing all about this bus.
- [ ] Bus search.
- [ ] Photo library for bus (in terms of model and/or fleet no.)
- [ ] _Data structure changed from `JSON` to `SQL` (TBA)_
- [ ] _Connection to AnyTrip (TBA depends on availability)_

## Screenshots

Will be available once the app becomes more complete...

---

> Note:
>
> This app is **NOT** a replacement for well-structured PT realtime apps like *AT Mobile*, *Google Maps*, *AnyTrip* or *Transit*. As an enthusiastic project, we have very limited resources available compared to these big companies and cannot offer auto-refresh functions in-app.

## Get started

This instruction will guide you through the preparation work before start using the app itself.

### Download and install

Go to the [Release](https://github.com/Kevincnzuk/live-ev-bus-akl/releases) page and seek the green "Latest" tag, and download the relevant `.apk` file.

Follow the instructions from your device to install it.

### Get an API Key

This step is mandatory as each subscriber has a per-minute and weekly cap.

Each subscriber (primary and secondary key counts together) can run 600 calls/minute up to a maximum of 35000 calls/week. To avoid my key exceeding this limit, it is better if every user uses their dedicated key.

Please also be advised that every refresh of the data set (the main list) **will count as one call**. All other functions, including map, use the data available in the list and thus do not make an extra call.

#### Step by step

1. Go to [Auckland Transport Developer Portal](https://dev-portal.at.govt.nz) and sign in / sign up for an account.
2. Subscribe to the `Public Transport Dev` channel.
3. Go to your `Profile`, under `Subscription` you will see a list like this:

![Key](images/keys.png)

5. Click `Show` and copy the `Primary Key` of the keys to the app's `Setting`, where an input field requires you to enter an `Primary Key`. You may also copy the `Secondary Key` in but is is optional.
6. Click `Save` on the top right, return to main page. Swipe down to refresh.

### Ready to use

You are ready to go!

## Troubleshooting

- [Data source currently unavailable.](no_datasets.md)

## Open-source project

This project would not be possible without these open-source projects.

- [Material Design](https://m3.material.io/) by Google
- [OkHttp](https://github.com/square/okhttp) by Square
- [Picasso](https://github.com/square/picasso) by Square
- [osmdroid](https://github.com/osmdroid/osmdroid) by osmdroid
- [PermissionX](https://github.com/guolindev/PermissionX) by Lin Guo

## License

This project is distributed under `GNU AFFERO GENERAL PUBLIC LICENSE`.

Read more at [`LICENSE`](LICENSE).
