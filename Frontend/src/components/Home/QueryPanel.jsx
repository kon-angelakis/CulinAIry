import SubmitButton from "../Forms/Elements/SubmitButton";
import Legend from "./Elements/Legend";
import QueryInput from "./Elements/QueryInput";
import FormInput from "../Forms/Elements/FormInput";
import { useState } from "react";

import "./QueryPanel.css";

export default function QueryPanel() {
  const [verified, setVerified] = useState(null);

  const [formData, setFormData] = useState({
    search_query: "",
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
  };
  return (
    <form className="root-container" onSubmit={handleSubmit} method="POST">
      <Legend />
      <div className="search-container">
        {/* <FormInput
          name="search_query"
          type="text"
          required={true}
          available={true}
          animationIndex={-1}
          onChange={handleChange}
        /> */}
        <QueryInput
          name="search_query"
          type="text"
          placeholder="What are you looking for?"
          value={formData.search_query}
          onChange={handleChange}
        />
        <SubmitButton
          text={"Search"}
          data={formData}
          endpoint="/api/ai/query"
          method="post"
          formAction="Query"
          isAuthenticatedRequest={true}
          setVerified={setVerified}
        />
      </div>
    </form>
  );
}
