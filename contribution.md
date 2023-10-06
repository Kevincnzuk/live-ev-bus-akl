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
   
   The file should then look like this:
   
   ```json
   [
       {"id": "10305","fleet": "NB0305","energy": "ev"},
       // ...
       {"id": "Old Bus","fleet": "Old Bus","energy": "Old bus"},
       {"id": "New Bus","fleet": "New Bus","energy": "New bus"}
   ]
   ```
   
   Please do notice that the last line **does not have a comma at the end, but all other lines do**.

5. Scroll to the top right, and commit change.

## Want to help maintain the code?

Email me at kchang753@outlook.com so I can add you to the contributor list.

## Can I download a copy of the source code and develop one for my city?

Yes, and that is one of the purposes I open-source it. Programming is a tool with unlimited imagination, we should get to use the most from it.

However, do pay attention to the conditions in GPL-3.0 license, you need to follow them if you uses copies of my code.
