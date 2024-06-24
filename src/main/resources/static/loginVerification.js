document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById("addUser");
    const usernameInput = document.getElementById("username");
    const emailInput = document.getElementById("email");
    const submitButton = document.getElementById("submit");

    const checkUsername = async (username) => {
        const response = await fetch(`/api/users/check-username?username=${encodeURIComponent(username)}`);
        return response.ok;
    };

    const checkEmail = async (email) => {
        const response = await fetch(`/api/users/check-email?email=${encodeURIComponent(email)}`);
        return response.ok;
    };

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        const username = usernameInput.value;
        const email = emailInput.value;

        const isUsernameAvailable = await checkUsername(username);
        const isEmailAvailable = await checkEmail(email);

        if (!isUsernameAvailable) {
            alert("해당 아이디는 이미 존재합니다.");
            usernameInput.focus();
            return;
        }

        if (!isEmailAvailable) {
            alert("해당 이메일은 이미 존재합니다.");
            emailInput.focus();
            return;
        }

        form.submit();
    });
});