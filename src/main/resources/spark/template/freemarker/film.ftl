<#assign content>

<div id = "film-header">
	<h2> Film: ${filmName} </h2>
</div>

<p> Actors in ${filmName}: </p>

<div class = "actors-in-film-header">
	<ul class = "actors-list">
	<#list actors as actor>
		<#if actor.getActorName() != "">
		<p><a href="/actor?actorName=${actor.getActorName()}">${actor.getActorName()}</a></p>
		</#if>
	</#list>
	</ul>
</div>

</#assign>
<#include "bacon.ftl">


