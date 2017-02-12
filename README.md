# kappka
Kedy Katka Pappka?

How to init app?

- add -> app/crashlyticsAssets/beta_distribution_emails.txt
- add -> app/crashlyticsAssets/beta_distribution_release_notes.txt
- add -> gradle.properties with crashlyticsApiKey key - value defined
- add -> app/fabric.properties file with 'apiSecret' defined

How to release app?

- edit app/build.gradle signingConfigs so that it matches your needs