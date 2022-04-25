    Accessing certificates from URLs provided
    
    Path : /students/certInfo

    Request :
            
            [
                {
                    "url": "https://www.linkedin.com"
                },
                {
                    "url": "https://www.facebook.com"
                },
                {
                    "url": "https://www.naukri.com"
                }
            ]

    Response : 

            [
                {
                    "version": "3",
                    "issuer": "DigiCert Inc",
                    "validFrom": "Mon Mar 28 05:30:00 IST 2022",
                    "validTo": "Thu Sep 29 05:29:59 IST 2022",
                    "subject": "www.linkedin.com",
                    "domainName": "www.linkedin.com",
                    "organization": "LinkedIn Corporation",
                    "algorithm": "SHA256withRSA",
                    "location": {
                        "region": "US",
                        "province": "California",
                        "locality": "Sunnyvale"
                    }
                },
                {
                    "version": "3",
                    "issuer": "DigiCert Inc",
                    "validFrom": "Tue Feb 01 05:30:00 IST 2022",
                    "validTo": "Tue May 03 05:29:59 IST 2022",
                    "subject": "*.facebook.com",
                    "domainName": "*.facebook.com",
                    "organization": "\"Facebook",
                    "algorithm": "SHA256withRSA",
                    "location": {
                        "region": "US",
                        "province": "California",
                        "locality": "Menlo Park"
                    }
                },
                {
                    "version": "3",
                    "issuer": "GlobalSign nv-sa",
                    "validFrom": "Wed Nov 17 18:11:08 IST 2021",
                    "validTo": "Mon Dec 19 18:11:08 IST 2022",
                    "subject": "*.naukri.com",
                    "domainName": "*.naukri.com",
                    "organization": "INFO EDGE (INDIA) LIMITED",
                    "algorithm": "SHA256withRSA",
                    "location": {
                        "region": "IN",
                        "province": "Uttar Pradesh",
                        "locality": "Noida"
                    }
                }
            ]