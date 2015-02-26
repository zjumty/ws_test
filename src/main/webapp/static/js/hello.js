var Comment = React.createClass({displayName: "Comment",
	render: function(){
		return (
			React.createElement("div", null, 
				React.createElement("h2", null, this.props.author), 
				React.createElement("p", null, this.props.text)
			)
		);
	}
});


var CommentList = React.createClass({displayName: "CommentList",
  render: function() {
  	var commentList = this.props.data.map(function(d){
  		return (
  			React.createElement(Comment, {author: d.author, text: d.text})
  		);
  	});
    return (
      React.createElement("div", {className: "commentList"}, 
        commentList
      )
    );
  }
});