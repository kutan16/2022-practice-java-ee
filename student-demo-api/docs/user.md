    Accessing certificates from URLs provided
    
    Path : /student/user/retrieve

    Method : POST

    Header : 
            Authorization : Bearer token

    Request :
            
            [
                {
                    "userId": "nilotpal"
                },
                {
                    "userId": "nilotpal4"
                },
                {
                    "userId": "nilotpal6"
                },
                {
                    "userId": "nilotpal"
                }
            ]

    Response : 
            [
                {
                    "user_id": "nilotpal",
                    "password": "password1",
                    "roles": "admin,user",
                    "scopes": "full"
                },
                {
                    "user_id": "nilotpal6",
                    "password": "password1",
                    "roles": "admin,user",
                    "scopes": "full"
                },
                {
                    "user_id": "nilotpal",
                    "password": "password1",
                    "roles": "admin,user",
                    "scopes": "full"
                }
            ]
