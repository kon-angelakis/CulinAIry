import axios from "axios";

function SubmitButton({ text, data, endpoint, disabled, setVerified }) {
  function PostData(e) {
    axios
      .post(endpoint, data)
      .then((response) => {
        console.log("Post Created:", response.data);
        setVerified(true); // Mark as verified if login succeeds
      })
      .catch((error) => {
        console.error("Error creating post:", error);
        setVerified(false); // Mark as not verified if login fails
      });
  }

  return (
    <>
      <button
        className="primary-button"
        onClick={PostData} // Call PostData on button click
        disabled={disabled}
      >
        {text}
      </button>
    </>
  );
}

export default SubmitButton;
