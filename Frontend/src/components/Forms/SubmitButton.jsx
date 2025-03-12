import axios from "axios";
import authAxios from "../../config/axiosConfig";

function SubmitButton({
  text, //buttons name
  data, //form data
  endpoint, //backend api clalcall
  method, //post/get/etc
  disabled, //condition for the button to be disabled
  setVerified, //condition indicating successful call and response
  formAction, //string containing a name for the buttons action ie(Login/Register/Send OTP)
  isAuthenticatedRequest, //handles authenticated or not requests
}) {
  //Handles requests where the user MUST be authenticated to retrieve or post data
  const handleAuthRequest = (method, endpoint, data, action) => {
    return authAxios[method](endpoint, data)
      .then((response) => {
        console.log(`${action} Successful:`, response.data);
        return response;
      })
      .catch((error) => {
        console.error(`${action} Error:`, error);
        throw error;
      });
  };

  //Handles normal requests like login or register
  const handleRequest = (method, endpoint, data, action) => {
    return axios[method]("http://localhost:1010" + endpoint, data)
      .then((response) => {
        console.log(`${action} Successful:`, response.data);
        return response;
      })
      .catch((error) => {
        console.error(`${action} Error:`, error);
        throw error; // Rethrow to catch in the main logic
      });
  };

  function PostData(e) {
    if (isAuthenticatedRequest) {
      handleAuthRequest(method, formAction, endpoint, data, formAction);
    } else {
      switch (formAction) {
        case "Login": //On login save the auth token and user details in the browsers local storage
          handleRequest(method, endpoint, data, formAction)
            .then((response) => {
              localStorage.setItem(
                "AuthToken",
                response.headers["authorization"].split("Bearer ")[1]
              );
              localStorage.setItem(
                "UserDetails",
                JSON.stringify(response.data.UserDetails)
              );
              setVerified(true);
            })
            .catch((error) => {
              setVerified(false);
            });
        default:
          handleRequest(method, endpoint, data, formAction)
            .then((response) => {
              setVerified(true);
            })
            .catch((error) => {
              setVerified(false);
              console.log(error);
            });
          break;
      }
    }
  }

  return (
    <>
      <button className="primary-button" onClick={PostData} disabled={disabled}>
        {text}
      </button>
    </>
  );
}

export default SubmitButton;
