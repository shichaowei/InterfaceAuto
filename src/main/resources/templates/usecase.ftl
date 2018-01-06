<!DOCTYPE html>
<html>
<#include "/include/head.ftl">
<body>

<div class="col-lg-10">
    		<form  role="form" action="/api/caseupload" method="post" enctype="multipart/form-data">
    			<div class="form-group">
    				<label for="name" class="control-label">选择文件:</label>
    				<input type="file" name="fileName"></input>
				</div>
	  			<div class="form-group">
	  				<button type="submit" class="btn btn-default">提交</button>
				</div>
			</form>
</div>


</body>
</html>

