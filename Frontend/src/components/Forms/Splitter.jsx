function Splitter({ text }) {
  return (
    <div className="splitter">
      <hr />
      {text && (
        <div className="splitter-text">
          <p> {text}: </p>
        </div>
      )}
      <hr />
    </div>
  );
}

export default Splitter;
