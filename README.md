To configure the Plivo
we need plivo account ( and registered 10DLC number for only US )
we need two pulic webhook urls
For development we can use ngrok or cloudflared to make the url public.

1. webhook callback status url : on this url plivo will send the meesage status.

![image](https://github.com/user-attachments/assets/39865f58-2681-4bb0-9708-0f0c609a1d1c)

2. reply url : on this url plivo will send the details of replied message by the end user.

![image](https://github.com/user-attachments/assets/8f0e8cc6-9248-4107-85ca-ca93baa76183)
