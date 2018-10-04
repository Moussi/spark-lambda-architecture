/**
  * Created by moussi on 24/02/18.
  */
package object domain {

  case class Activity(timestamp_hour: Long,
                      referrer: String,
                      action: String,
                      prevPage: String,
                      page: String,
                      visitor: String,
                      product: String,
                      inputProps: Map[String, String] = Map()
                     )

    case class AudiencePoi(poi_id: String,
                           nb: String)
    case class AudiencePoi2(poi_id: String,
                                              nb: Long,
                                              indicateur: String,
                                              tagid: String,
                                              tagid_groupe: String,
                                              tagid_sous_groupe: String,
                                              is_bot: Boolean,
                                              terminal: String,
                                              uad_terminal: String,
                                              env: String,
                                              id: String,
                                              epjid: String,
                                              offertype: String,
                                              name: String,
                                              town: String,
                                              postalcode: String,
                                              region: String,
                                              country: String,
                                              rubricid: String,
                                              rubrique_parent: String,
                                              rubrique_categorie: String,
                                              rubrique_bu: String,
                                              rubrique_bu_segment: String,
                                              ovm: String,
                                              additionalrubrics: String,
                                              mainprovider: String,
                                              brand: String,
                                              onumcli: String,
                                              visibilitylevel: String,
                                              localbusinesspackage: String,
                                              providers: String,
                                              allrubrics: String,
                                              storechains: String,
                                              pjrating: String,
                                              indoorview: String,
                                              allappids: String,
                                              tabsappid: String,
                                              additionalinfoappids: String,
                                              cvivcount: String,
                                              cvi: String,
                                              pvi: String,
                                              pjcontent: String,
                                              indexedrubricids: String,
                                              towncode: String,
                                              loc_lng: Option[Double],
                                              loc_lat: Option[Double],
                                              way: String,
                                              geocodinglevel: String,
                                              geocodingstrategy: String,
                                              pjinfo: String,
                                              repositioning: String,
                                              outdoorview: String,
                                              gpsfree_pubid: String,
                                              gpsfree_mode: String,
                                              gpsfree_numcli: String,
                                              gpsfree_datemaj: String,
                                              phone: String,
                                              ovmdata_pubid: String,
                                              ovmdata_datemaj: String,
                                              ovmdata_numcli: String,
                                              lastoffertypechange: Option[Long],
                                              vdeappids: String,
                                              tags: String,

                                              cornerappids: String)

  case class ActivityByProduct(product: String,
                               timestamp_hour: Long,
                               purchaseCount: Long,
                               addToCardCount: Long,
                               pageViewCount: Long
                              )

  case class VisitorsByProduct(product: String,
                               timestamp_hour: Long,
                               unique_visitors: Long
                              )

}
