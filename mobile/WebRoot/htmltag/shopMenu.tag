<ul class="nav nav-tabs">
	##if(exist("url") && url=="/shop/article"){
		<li class="active">
			<a href="/shop/article" style="padding: 15px 20px;">
				<span style="margin-left: 5px;">文章管理</span>
			</a>
		</li>
	##}else{
		<li>
			<a href="/shop/article" style="padding: 15px 20px;">
				<span style="margin-left: 5px;">文章管理</span>
			</a>
		</li>
	##}
	##if(exist("url") && url=="/shop/freight"){
		<li class="active">
			<a href="/shop/freight" style="padding: 15px 20px;">
				<span style="margin-left: 5px;">运费模板</span>
			</a>
		</li>
	##}else{
		<li>
			<a href="/shop/freight" style="padding: 15px 20px;">
				<span style="margin-left: 5px;">运费模板</span>
			</a>
		</li>
	##}
	##if(exist("url") && url=="/shop/carousel"){
		<li class="active">
			<a href="/shop/carousel" style="padding: 15px 20px;">
				<span style="margin-left: 5px;">轮播图</span>
			</a>
		</li>
	##}else{
		<li>
			<a href="/shop/carousel" style="padding: 15px 20px;">
				<span style="margin-left: 5px;">轮播图</span>
			</a>
		</li>
	##}
</ul>