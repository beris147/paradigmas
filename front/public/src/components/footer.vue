<template>
    <footer class="page-footer font-small pt-4">
      <hr>
      <ul class="list-unstyled list-inline text-center">
        <li class="list-inline-item">
          <a class="btn-floating btn-fb mx-1 white"
            href="https://www.facebook.com/chismenolikeoficial/?__tn__=kC-R&eid=ARAVcBRwbO_fvhuyeCy6ouDriLxWDYHv3m3V92HqmeOA3S5scaay0hhnEOcefSFMJoTS3HO850UDOISu&hc_ref=ARRFn0goJ3w9Qh4mVQj3ZlKn0Amt1fhP35vvQrx1TiNlAHFLy3wTRpu_shVqpKBkJ3M&fref=nf&__xts__[0]=68.ARABiDdjRoE6nIWrVd4QSbEkQNeuQUNL5wSuQ63JxQD2bUHqBn0dbuJaoMtACn49C1cP57zsU3XFM3JCE2Hs2Q-KZctnDwpsJQWtFieZPNTCpnhytzmhuwGTRfJxypCLGlLNoZkUibCNPHZoq7sW-K0n_UR5zdfCT7HpwR0xvjeoFbiVVY2i5wSXntR_Z4qkssdblnXIYIP-oh9TGXOgScmkAk2-7HG4yNDilzooPdSDdVmyYJlsZHSgmx2IVAkDRI2pVK8_kuHpoHYiwXLEExK4jTk7jDG3TnS1--A8xHYHfSBSEXxR63q-KMlvrQk-ZYnBwyDGROb-0L4" target="_blank">
            <font-awesome-icon :icon="{ prefix: 'fab', iconName: 'facebook-f' }"/>
          </a>
        </li>
        <li class="list-inline-item">
          <a class="btn-floating btn-tw mx-1 white"
            href="https://twitter.com/ChismeNo" target="_blank">
            <font-awesome-icon :icon="{ prefix: 'fab', iconName: 'twitter' }"/>
          </a>
        </li>
        <li class="list-inline-item">
          <a class="btn-floating btn-li mx-1 white"
            href="https://www.instagram.com/chismenolikeoficial/" target="_blank">
            <font-awesome-icon :icon="{ prefix: 'fab', iconName: 'instagram' }"/>
          </a>
        </li>
        <li class="list-inline-item">
          <a class="btn-floating btn-dribbble mx-1 white"
            href="https://www.youtube.com/channel/UC5qgk9xFZhXjzvCRcZn8KqQ" target="_blank">
            <font-awesome-icon :icon="{ prefix: 'fab', iconName: 'youtube' }"/>
          </a>
        </li>
      </ul>
      <div class="footer-copyright text-center pt-5">
         <p class="btn-floating btn-dribbble mx-1 white">
            Visita
          </p>
      </div>
      <div class="footer-copyright text-center pb-3">
          <a class="btn-floating btn-dribbble mx-1 white" href="http://beristyle.com/" target="_blank">Beristyle</a>
          <a class="btn-floating btn-dribbble mx-1 white" href="http://www.elisaberistain.com/" target="_blank">Elisa Beristain</a>
      </div>
    </footer>
</template>

<script>
    export default {
        name: 'Footer'
    }
</script>

<style scope>
  footer {
    background-color: #000000 !important;
    color: white;
  }

  .white {
    color: #F6CEF5 !important;
  }
</style>