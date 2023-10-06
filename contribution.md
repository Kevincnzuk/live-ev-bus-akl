# How to maintain this repository

A general guide on future maintenance of this repository and the service it provides.

> Repository location: [https://github.com/Kevincnzuk/live-ev-bus-akl](https://github.com/Kevincnzuk/live-ev-bus-akl).

## Adding new bus

1. Find the `akl_ev.json` file under the folder `datasets`.

2. First search the bus you want to contribute, in case someone else has already added it. If not, continue to step 3.

3. Click "Edit" on the top right (should be a pencil icon).

4. Before the square bracket `]` at the end of the file, add this line:
   
   ```json
   {"id": "","fleet": "","energy": ""},
   ```
   
   Where `id` should be a 5-digit number which you can find using AnyTrip. `fleet` should always have a length of 6, if 5 (eg `HE505`) then add a `0` after letters (`HE0505`). Energy should be either `ev` for electric vehicle, `fcv` for fuel cell vehicle, or `hev` for hybrid electric vehicle.
   
   The file should then look something similar to this:
   
   ```json
   [
       {"id": "10305","fleet": "NB0305","energy": "ev"},
       {"id": "Lots","fleet": "Of","energy": "Entries"},
       {"id": "12345","fleet": "AB2345","energy": "ev"},
       {"id": "12346","fleet": "AB2346","energy": "ev"}
   ]
   ```
   
   Please do notice that the last line **does not have a comma at the end, but all other lines do**.

5. Scroll to the top right, and commit change.

## Want to help maintain the code?

Email me at `kchang753@outlook.com` so I can add you to the contributor list.
