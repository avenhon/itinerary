# 🛫Itinerary Prettifier

**A tool for fully automating the process of formatting itinerary is customer-friendly.**

>When a customer buys a flight, a back-office administrator will use an online-portal to reserve the flight for the customer. That portal generates a text-based itinerary, which can be downloaded as a text file to the administrator's computer.

---
## 🔌Usage of the tool
Before use, you need to install all the necessary files: 

`https://gitea.kood.tech/yuriipankevych/itinerary.git`;

`cd itinerary`;

`java Prettifier.java <input_path> <output_path> <airport_lookup_path>`.

For example:
`java Prettifier.java ./input.txt ./output.txt ./airport-lookup.csv`.

### 🚩Flags
`java Prettifier.java -h` - for output help messages in the console.

`java Prettifier.java -o <input_path> <output_path> <airport_lookup_path>` - for output formatted result in the console.

---
## 📄Input format
### 📆Dates:
`D(YYYY-MM-DDTHH:MM-HH:MM)` - **DD-Mmm-YYYY**.

**For example:**
```
D(2007-04-05T12:30−02:00) → 05 Apr 2007
```

### ⌚Times
**12 Hour time:** `T12(YYYY-MM-DDTHH:MM-HH:MM)`

**24 Hour time:** `T24(YYYY-MM-DDTHH:MM-HH:MM)`

**For example:** 
```
T12(2007-04-05T12:30−02:00) → 12:30PM (-02:00)
T12(1980-02-17T03:30+11:00) → 03:30AM (+11:00)
T24(2007-04-05T12:30−02:00) → 12:30 (-02:00)
T24(2084-04-13T17:54Z) → 17:54 (+00:00)
```
### 🎲IATA and ICAO codes
**IATA code**: `#***`

**ICAO code**: `##****`

Tool converts the IATA and ICAO codes to airport names. But if you want to convert IATA and ICAO codes to city names, you can use the `*` character before the IATA or ICAO code.

**For example:**
```
#HAJ → Hannover Airport
*#HAJ → Hannover
##EDDW → Bremen Airport
*##EDDW → Bremen
```

---
## 🛫📄Airport Lookup

### Columns meaning:
- `name` - name of airport.
- `iso_country` - code that represents the name of the country.
- `municipality` - municipality where the airport located.
- `icao_code` - ICAO code of airport.
- `iata_code` - IATA code of airport.
- `coordinates` - latitude and longitude.

---
## ✨Extra Features and Bonuses
- **❌Error Highlighting**: When you enter invalid file paths, you receive errors that have a certain color.
- **✅Usage Highlighting**: When you enter the incorrect usage command, you receive a help message, which also has a certain color.
- **🚩Flag for output**: When you use the `-o` flag, the program will output the entire formatted data into the console.
- **📃Documentation**: The documentation you are reading right now!
---
## 🎆Credentials
Tool created by **Yurii Pankevych**. Thank you for your attention😇