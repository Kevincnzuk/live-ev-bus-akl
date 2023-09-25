# Why am I seeing "Data source currently unavailable."?

This could result from multiple reasons, some possibilities are listed below.

1. No bus is operating - Between 2 am and 5 am literally no buses are out running, including diesel. Wait till the first bus of the day is out and running (normally after 5 am). Or if extreme weather approaches, AT may suspend all buses till it is safe to do so, but do check [AT Travel Alerts](https://twitter.com/AT_TravelAlerts) for confirmation.
2. You have no internet connection - Unlikely if you can still see this text. You could [do a speed test](https://www.chorus.co.nz/speed-test).
3. Auckland Transport's API is down - Check if you can access [https://dev-portal.at.govt.nz/](https://dev-portal.at.govt.nz/) and send a request. [How to send a request using a browser?](#how-to-send-a-request-using-a-browser)
4. You have entered the wrong key - Check if the keys you have entered in `Settings` are the same as it is shown in your [account](https://dev-portal.at.govt.nz/signin).
5. Other reasons - Pull a [issue](https://github.com/Kevincnzuk/live-ev-bus-akl/issues) here on GitHub and I will have a look. Attach as much information as you possibly can.

## How to send a request using a browser?

> This `JSON` will be pretty big compared to the one app sent, so I recommend a WiFi connection.

1. Sign in to your developer account at [https://dev-portal.at.govt.nz/](https://dev-portal.at.govt.nz/).
2. On the top right navigation bar, click `APIs`.
3. In the list, click `Realtime Compat`.
4. On the left side, click `Vehicle Positions` in the list.
5. On the right side, click on the green `Try It` button.
6. In the pop-up panel, scroll to the bottom, and click `Send`.
7. A `JSON` file containing all vehicles and trains operating will be displayed on your browser, see if it contains anything bus-related (e.g. a fleet number `"label": "NB5835"`).
