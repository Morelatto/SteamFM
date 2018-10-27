<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
    /* NOTE: The styles were added inline because Prefixfree needs access to your styles and they must be inlined if they are on local disk! */
    html,
    body {
        background: #212121;
        font-family: Helvetica, sans-serif;
        font-size: 12px;
    }

    .main-container {
        margin: 4em auto 0 auto;
        width: 650px;
    }

    .main-container .controls {
        margin: 0 auto;
        padding: 2em 0 0 0;
        text-align: center;
    }

    .coverflow-container {
        box-reflect: below 0px -webkit-linear-gradient(top, rgba(0, 0, 0, 0) 60%, rgba(0, 0, 0, 0.1) 100%);
    }

    label {
        color: #999;
        display: inline-block;
        padding: 10px;
        border: 1px solid black;
        cursor: pointer;
        background: #252525;
        text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.9);
    }

    label:hover {
        color: #FFF;
    }

    .coverflow-list {
        width: 100%;
    }

    .coverflow-list .coverflow-item {
        display: inline-block;
        background: #414141;
        margin: 0 -50px;
        position: relative;
        box-shadow: 0 1px 8px rgba(0, 0, 0, 0.9);
    }

    .coverflow-list .coverflow-item .album-cover {
        display: block;
        height: 150px;
        margin: 0;
    }

    .coverflow-list .coverflow-item .album-cover img {
        height: 150px;
        width: 150px;
        opacity: 0.5;
    }

    .coverflow-list .coverflow-item .album-name {
        text-align: center;
        display: block;
        color: #444;
    }

    .coverflow-list .coverflow-item label {
        padding: 0;
        border: none;
        display: block;
        text-shadow: none;
    }

    /* Now apply 3D transforms (never done this before!) */
    .coverflow-list {
        -webkit-transform: perspective(900px);
        -webkit-transform-style: preserve-3d;
        -webkit-perspective-origin: 100% 30%;
    }

    .coverflow-list .coverflow-item {
        -webkit-transition: all 0.4s ease;
        background: #212121;
        -webkit-transform: rotateY(45deg);
    }

    input[type="radio"] {
        display: none;
    }

    input[type="radio"]:checked + .coverflow-item {
        -webkit-transform: rotateY(0deg);
        margin: 0 auto;
        background: #313131;
        transition: all 0.4s ease;
    }

    input[type="radio"]:checked + .coverflow-item img {
        opacity: 1;
    }

    input[type="radio"]:checked + .coverflow-item figcaption {
        color: #FFFFFF;
    }

    input[type="radio"]:checked + .coverflow-item ~ .coverflow-item {
        -webkit-transform: rotateY(-45deg);
        background: #515151;
    }

    /*@keyframes cover-forward {
      0% { transform: rotateY( 45deg ); }
      50% { transform: rotateY( 0deg ) translate3d( 60px,0,0 ) scale3d(1.2,1.2,1.2); z-index:999; }
      100% { transform: rotateY( 45deg ) translate3d( 0,0,0 ) scale3d(1,1,1); }
    }
    
    @keyframes cover-return {
      0% { transform: rotateY( 45deg ); }
      100% { transform: rotateY( 0deg ) translate3d( 150px,0,0 ) scale3d(1.2,1.2,1.2); }
    }*/

</style>
<script src="js/prefixfree.min.js"></script>
<!--- This may need to change --->
<div class="main-container">
    <div class="coverflow-container">
        <div class="coverflow-list">
            <!-- Cover item -->
            <c:set var="j" value="0"></c:set>
            <c:forEach items="${game.getListaRecomendacao()}" var="recomendacao">
                <c:set var="j" value="${j+1}"></c:set>
                <input type="radio" name="cover-item" id="cover-${j}">
                <li class="coverflow-item">
                    <label for="cover-${j}">
                        <figure class="album-cover">
                            <img src="${recomendacao.getImagem()}">
                            <figcaption class="album-name">${recomendacao.getNome()}</figcaption>
                        </figure>
                    </label>
                </li>
            </c:forEach>
        </div>
    </div>
</div>
