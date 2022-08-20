# Collection and search of exchange rates

### Description

Web server for collection and search of official exchange rates of Azerbaijani manat against foreign currencies.
Exchange rates for the specific date is collected from the following link:
https://www.cbar.az/currencies/25.05.2022.xml

## Set up the app

* `MS Sql Server 2016` or higher version have to be accessible for environment.
  For more information about `docker` images for `MS SQL Server` go
  [here](https://hub.docker.com/_/microsoft-mssql-server).

* The following environment variables have to be defined:
    * `${DB_URL}`
    * `${DB_USERNAME}`
    * `${DB_PASSWORD}`

## Test the app

Internet connection must be enabled for Integration testing.

## Run the app

All APIs require authentication. There are 2 types of authentication in app. `Basic` and `Bearer`

### Credentials

* Basic
    * **username:** user
    * **password:** 123456
* Bearer
    * **token:** 1234567890

### APIs

* Collect currencies
    * **method:** `GET`
    * **url:** `/currencies/{date}.json` Here `date` have to be in a `dd.MM.yyyy` format.
    * **auth type:** `Bearer`
    * **success status** 200
    * **returns:** [CollectResultDto](#collectresultdto)
* Delete currencies for specific date
    * **method:** `DELETE`
    * **url:** `/currencies/{date}.json` Here `date` have to be in a `dd.MM.yyyy` format.
    * **auth type:** `Bearer`
    * **success status** 204
    * **returns:** nothing
* Get currencies from db
    *
        * **method:** `GET`
    * **url:** `/currencies` Here `date` have to be in a `dd.MM.yyyy` format.
    * **filter parameters**: Following parameters are non-mandatory and makes composition while using together.
        * `date` - filter for AZN currencies in a given date. Matching rule is `Equal`. Parameter have to be in
          a `dd.MM.yyyy` format.
        * `code` - filter for AZN currencies against given symbol. The parameter have to contain 3 Uppercase Letter. For
          more details go [here](https://www.cbar.az/currency/rates).
        * `page` - number of the page of result.
        * `size` - number of elements in a page
    * **auth type:** `Basic`
    * **success status** 200
    * **returns:** instance of `org.springframework.data.domain.Page' with type of of
      the [CurrencyConversionDto](#currencyconversiondto)

### Models

#### `CollectResultDto`

* `code` - operation status code. ([Status Codes](#status-codes))
* `message` - detailed message about operation status.

#### `CurrencyConversionDto`

* `date`
* `type`
* `code`
* `nominal`
* `name`
* `value`

### Values

#### Status Codes

* `"0000"` - success
* `"0001"` - exists in db
* `"0002"` - no data found from external api
* `"0003"` - external exception 
